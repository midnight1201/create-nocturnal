package ver.faker.nocturnal.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;

import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.content.vampire_piston.VampirePistonBlockEntity;
import ver.faker.nocturnal.content.vampire_piston.VampirePistonRenderer;

public class NocBlockEntities {

    public static final BlockEntityEntry<VampirePistonBlockEntity> VAMPIRE_PISTON_BE = Nocturnal.REGISTRATE
            .blockEntity("vampire_piston", VampirePistonBlockEntity::new)
            .validBlock(NocBlocks.VAMPIRE_PISTON)
            .renderer(() -> VampirePistonRenderer::new)
            .register();

    public static void register() {
        Nocturnal.LOGGER.info("Register Block Entities...");
    }
}