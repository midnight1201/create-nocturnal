package ver.faker.nocturnal.content.vampire_piston;

import java.util.List;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

public class VampirePistonBlockEntity extends GeneratingKineticBlockEntity {

    // Temporary: spins at a fixed speed until fluid input drives generation
    public static final float GENERATION_SPEED = 16f;

    public VampirePistonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        // TODO: add SmartFluidTankBehaviour exposed on the south (FACING) face
        //       once the custom fluid is implemented; gate getGeneratedSpeed() on its contents.
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {
        return convertToDirection(GENERATION_SPEED, getBlockState().getValue(VampirePistonBlock.FACING));
    }

    public static final float MAX_OFFSET = 2f / 16f;   // 2 pixels, in block units
    private static final float SPEED = 0.05f;          // bob frequency; tune to taste
    public float getBitOffset(float partialTicks) {
        // Only bob while actually producing rotation
        if (getSpeed() == 0)
            return 0f;
        float time = net.createmod.catnip.animation.AnimationTickHolder.getRenderTime(getLevel());
        float wave = Mth.sin((time + partialTicks) * SPEED);   // -1..1
        return (wave * 0.5f + 0.5f) * MAX_OFFSET;              // 0..2px, piston-like bob
    }
}