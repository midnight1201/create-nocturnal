package ver.faker.nocturnal.content.vampire_piston;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.world.level.block.entity.BlockEntityType;

import ver.faker.nocturnal.register.NocBlockEntities;

public class VampirePistonBlock extends DirectionalAxisKineticBlock implements IBE<VampirePistonBlockEntity> {

    public VampirePistonBlock(Properties properties) {
        super(properties);
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