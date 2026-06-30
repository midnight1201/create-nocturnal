package ver.faker.nocturnal.content.vampire_piston;

import java.util.List;
import java.util.Objects;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.CreateLang;

import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.nbt.NBTHelper;
import net.midnight.hemodynamics.api.BloodFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class VampirePistonBlockEntity extends GeneratingKineticBlockEntity implements IMultiBlockEntityContainer.Fluid {

    public static final int CAPACITY_PER_BLOCK = 1000;
    public static final int CONSUMPTION = 1;           // mb drained per draw
    public static final int CONSUME_INTERVAL = 5;      // ticks between draws
    public static final float GENERATION_SPEED = 32f;  // RPM

    // Bit animation
    public static final float MAX_OFFSET = 2f / 16f;   // stroke = 2 px
    private static final float ROD_RATIO = 0.5f;       // r / L; higher = more asymmetric motion
    private static final float PUMP_RATIO = 1f;        // crank revs per shaft rev
    private static final float RAD_PER_TICK_PER_RPM = (float) (Math.PI * 2.0 / (60.0 * 20.0));
    private static final float RPM_EASING = 0.35f;     // how quickly the bob follows rpm changes

    // Fluid + multiblock
    protected SmartFluidTank tankInventory;
    protected IFluidHandler fluidCapability;
    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected int length = 1;
    protected boolean updateConnectivity = false;
    protected boolean updateCapability = false;

    protected ScrollOptionBehaviour<WindmillBearingBlockEntity.RotationDirection> movementDirection;

    private float crankAngle = 0f;
    private float prevCrankAngle = 0f;
    private float easedRpm = 0f;

    public VampirePistonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        tankInventory = new SmartFluidTank(CAPACITY_PER_BLOCK, this::onFluidChanged);
        tankInventory.setValidator(fs -> fs.getFluid() == BloodFluids.source());

        movementDirection = new ScrollOptionBehaviour<>(WindmillBearingBlockEntity.RotationDirection.class,
                CreateLang.translateDirect("contraptions.windmill.rotation_direction"), this, new VampirePistonValueBox());
        movementDirection.withCallback(this::onDirectionChanged);
        behaviours.add(movementDirection);
    }

    // ---- Connectivity / capability ----

    public void updateConnectivity() {
        updateConnectivity = false;
        assert level != null;
        if (level.isClientSide || !isController()) return;
        ConnectivityHandler.formMulti(this);
    }

    void refreshCapability() {
        fluidCapability = handlerForCapability();
        invalidateCapabilities();
    }

    private IFluidHandler handlerForCapability() {
        if (isController()) return tankInventory;
        return getControllerBE() != null ? getControllerBE().handlerForCapability() : new FluidTank(0);
    }

    public IFluidHandler getFluidCapability() {
        if (fluidCapability == null) refreshCapability();
        return fluidCapability;
    }

    // ---- Rotation direction sync ----

    public void onDirectionChanged(int v) {
        if (level == null) return;
        VampirePistonBlockEntity controller = getControllerBE();
        if (controller == null) return;
        controller.movementDirection.setValue(v);
        controller.reActivateSource = true;
        Direction.Axis axis = controller.getMainConnectionAxis();
        for (int i = 0; i < controller.getHeight(); i++)
            if (level.getBlockEntity(controller.getBlockPos().relative(axis, i)) instanceof VampirePistonBlockEntity be
                    && be.movementDirection.getValue() != v)
                be.movementDirection.setValue(v);
    }

    // ---- Generation ----

    @Override
    public float getGeneratedSpeed() {
        if (!isController() || noFuel()) return 0;
        float speed = GENERATION_SPEED * (movementDirection.getValue() == 1 ? -1 : 1);
        return convertToDirection(speed, getBlockState().getValue(VampirePistonBlock.FACING));
    }

    @Override
    public float calculateAddedStressCapacity() {
        float capacity = 256f * getHeight();   // scale SU with line length
        this.lastCapacityProvided = capacity;
        return capacity;
    }

    public boolean noFuel() {
        return tankInventory.isEmpty();
    }

    private void onFluidChanged(FluidStack fs) {
        if (level == null || level.isClientSide) return;
        updateGeneratedRotation();
        setChanged();
        sendData();
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }

    // ---- Tick ----

    @Override
    public void tick() {
        super.tick();
        if (updateCapability) { updateCapability = false; refreshCapability(); }
        if (updateConnectivity) updateConnectivity();

        // bit bob follows shaft rpm (eased so it spins up / winds down smoothly)
        prevCrankAngle = crankAngle;
        easedRpm = Mth.lerp(RPM_EASING, easedRpm, getSpeed());
        crankAngle += easedRpm * RAD_PER_TICK_PER_RPM * PUMP_RATIO;

        // fuel consumption (controller, server only)
        if (level == null || level.isClientSide || !isController()) return;
        if (noFuel() || getSpeed() == 0) return;
        if (level.getGameTime() % CONSUME_INTERVAL == 0)
            tankInventory.drain(CONSUMPTION * getHeight(), IFluidHandler.FluidAction.EXECUTE);
    }

    public float getBitOffset(float partialTicks) {
        float theta = Mth.lerp(partialTicks, prevCrankAngle, crankAngle);

        // alternate default piston state on world position
        BlockPos p = getBlockPos();
        if (((p.getX() + p.getY() + p.getZ()) & 1) != 0)
            theta += Mth.PI;

        float r = MAX_OFFSET * 0.5f;                 // crank radius = half the 2 px stroke
        float sin = Mth.sin(theta);
        return r * (1f - Mth.cos(theta))
                + (r / ROD_RATIO) * (1f - (float) Math.sqrt(1f - ROD_RATIO * ROD_RATIO * sin * sin));
    }

    // ---- Goggles ----

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!isController()) {
            VampirePistonBlockEntity controller = getControllerBE();
            return controller != null && controller.addToGoggleTooltip(tooltip, isPlayerSneaking);
        }
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        added |= containedFluidTooltip(tooltip, isPlayerSneaking, getFluidCapability());
        return added;
    }

    // ---- NBT ----

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        BlockPos controllerBefore = controller;
        int prevLength = length;

        updateConnectivity = compound.contains("Uninitialized");
        controller = null;
        lastKnownPos = null;
        if (compound.contains("LastKnownPos"))
            lastKnownPos = NBTHelper.readBlockPos(compound, "LastKnownPos");
        if (compound.contains("Controller"))
            controller = NBTHelper.readBlockPos(compound, "Controller");

        if (isController()) {
            length = compound.getInt("Length");
            tankInventory.setCapacity(CAPACITY_PER_BLOCK);
            tankInventory.readFromNBT(registries, compound.getCompound("TankContent"));
            if (tankInventory.getSpace() < 0)
                tankInventory.drain(-tankInventory.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }
        updateCapability = true;

        if (!clientPacket) return;
        boolean changeOfController = !Objects.equals(controllerBefore, controller);
        if (changeOfController || prevLength != length) {
            if (hasLevel()) {
                assert level != null;
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            }
            invalidateRenderBoundingBox();
        }
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        if (updateConnectivity)
            compound.putBoolean("Uninitialized", true);
        if (lastKnownPos != null)
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (!isController())
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        if (isController()) {
            compound.putInt("Length", length);
            compound.put("TankContent", tankInventory.writeToNBT(registries, new CompoundTag()));
        }
    }

    // ---- IMultiBlockEntityContainer.Fluid ----

    @Override public boolean hasTank() { return true; }
    @Override public int getTankSize(int tank) { return tankInventory.getCapacity(); }
    @Override public void setTankSize(int tank, int blocks) {
        tankInventory.setCapacity(CAPACITY_PER_BLOCK);   // single shared buffer
    }
    @Override public IFluidTank getTank(int tank) { return tankInventory; }
    @Override public FluidStack getFluid(int tank) { return tankInventory.getFluid(); }

    @Override public Direction.Axis getMainConnectionAxis() {
        return ((VampirePistonBlock) getBlockState().getBlock()).getRotationAxis(getBlockState());  // shaft axis
    }
    @Override public int getMaxLength(Direction.Axis longAxis, int width) { return 16; }
    @Override public int getMaxWidth() { return 1; }
    @Override public int getHeight() { return length; }
    @Override public void setHeight(int height) { length = height; }
    @Override public int getWidth() { return 1; }
    @Override public void setWidth(int width) {}

    @Override public BlockPos getController() { return isController() ? worldPosition : controller; }
    @Override public boolean isController() { return controller == null || controller.equals(worldPosition); }
    @Override public VampirePistonBlockEntity getControllerBE() {
        if (isController() || !hasLevel()) return this;
        assert level != null;
        return level.getBlockEntity(controller) instanceof VampirePistonBlockEntity be ? be : null;
    }
    @Override public void setController(BlockPos controller) {
        assert level != null;
        if (level.isClientSide && !isVirtual()) return;
        if (controller.equals(this.controller)) return;
        this.controller = controller;
        refreshCapability();
        setChanged();
        sendData();
    }
    @Override public void removeController(boolean keepContents) {
        assert level != null;
        if (level.isClientSide) return;
        updateConnectivity = true;
        controller = null;
        length = 1;
        reActivateSource = true;
        refreshCapability();
        setChanged();
        sendData();
    }
    @Override public BlockPos getLastKnownPos() { return lastKnownPos; }
    @Override public void preventConnectivityUpdate() { updateConnectivity = false; }
    @Override public void notifyMultiUpdated() { reActivateSource = true; setChanged(); }

    // ---- Value box (CW/CCW toggle on bare casing faces) ----

    public static class VampirePistonValueBox extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 16);
        }
        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            if (!(state.getBlock() instanceof VampirePistonBlock block)) return false;
            Direction.Axis pipe = state.getValue(VampirePistonBlock.FACING).getAxis();
            Direction.Axis shaft = block.getRotationAxis(state);
            return direction.getAxis() != pipe && direction.getAxis() != shaft;
        }
    }
}