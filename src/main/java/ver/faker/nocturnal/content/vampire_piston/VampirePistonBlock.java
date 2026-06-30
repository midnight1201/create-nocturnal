package ver.faker.nocturnal.content.vampire_piston;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraft.world.level.block.state.BlockState;
import ver.faker.nocturnal.register.NocBlockEntities;

public class VampirePistonBlock extends DirectionalAxisKineticBlock implements IBE<VampirePistonBlockEntity> {

    public VampirePistonBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() == state.getBlock() || isMoving)
            super.onPlace(state, level, pos, oldState, isMoving);
        withBlockEntityDo(level, pos, VampirePistonBlockEntity::updateConnectivity);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())
                && level.getBlockEntity(pos) instanceof VampirePistonBlockEntity be) {
            level.removeBlockEntity(pos);
            ConnectivityHandler.splitMulti(be);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public Class<VampirePistonBlockEntity> getBlockEntityClass() {
        return VampirePistonBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends VampirePistonBlockEntity> getBlockEntityType() {
        return NocBlockEntities.VAMPIRE_PISTON_BE.get();
    }
}