package ver.faker.nocturnal.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.content.vampire_piston.VampirePistonBlock;
import ver.faker.nocturnal.content.vampire_piston.VampirePistonBlockEntity;
import ver.faker.nocturnal.content.vampire_piston.VampirePistonRenderer;

public class NocBlockEntities {

    public static final BlockEntityEntry<VampirePistonBlockEntity> VAMPIRE_PISTON_BE = Nocturnal.REGISTRATE
            .blockEntity("vampire_piston", VampirePistonBlockEntity::new)
            .validBlock(NocBlocks.VAMPIRE_PISTON)
            .renderer(() -> VampirePistonRenderer::new)
            .register();

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                VAMPIRE_PISTON_BE.get(),
                (be, side) -> {
                    Direction inputFace = be.getBlockState().getValue(VampirePistonBlock.FACING).getOpposite();
                    if (side != null && side != inputFace) return null;
                    return be.getFluidCapability();      // controller-owned tank
                });
    }

    public static void register() {
        Nocturnal.LOGGER.info("Register Block Entities...");
    }
}