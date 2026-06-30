package ver.faker.nocturnal.register;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.content.NocSpriteShifts;
import ver.faker.nocturnal.content.vampire_piston.VampirePistonBlock;

import static ver.faker.nocturnal.content.NocBlockStateGen.directionalAxisBlockUvLocked;

public class NocBlocks {

    public static final BlockEntry<CasingBlock> VAMPIRE_CASING = Nocturnal.REGISTRATE
            .block("vampire_casing", CasingBlock::new)
            .transform(BuilderTransformers.casing(() -> NocSpriteShifts.VAMPIRE_CASING))
            .register();

    public static final BlockEntry<EncasedShaftBlock> VAMPIRE_ENCASED_SHAFT = Nocturnal.REGISTRATE
            .block("vampire_encased_shaft", p -> new EncasedShaftBlock(p, NocBlocks.VAMPIRE_CASING::get))
            .initialProperties(() -> Blocks.ANDESITE)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .loot((p, lb) -> p.dropOther(lb, AllBlocks.SHAFT.get()))
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(NocSpriteShifts.VAMPIRE_CASING)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, NocSpriteShifts.VAMPIRE_CASING,
                    (s, f) -> f.getAxis() != s.getValue(EncasedShaftBlock.AXIS))))
            .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, blockState -> p.models()
                    .getExistingFile(p.modLoc("block/vampire/encased_shaft/block")), true))
            .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/vampire/encased_shaft/item")))
            .build()
            .register();

    public static final BlockEntry<VampirePistonBlock> VAMPIRE_PISTON = Nocturnal.REGISTRATE
            .block("vampire_piston", VampirePistonBlock::new)
            .initialProperties(() -> Blocks.ANDESITE)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .onRegister(block -> BlockStressValues.CAPACITIES.register(block, () -> 256.0))
            .onRegister(BlockStressValues.setGeneratorSpeed(16))
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(NocSpriteShifts.VAMPIRE_CASING)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, NocSpriteShifts.VAMPIRE_CASING,
                    (state, face) -> {
                        Direction.Axis pipeAxis = state.getValue(VampirePistonBlock.FACING).getAxis();
                        Direction.Axis shaftAxis = block.getRotationAxis(state);
                        return face.getAxis() != pipeAxis && face.getAxis() != shaftAxis;
                    })))
            .blockstate((ctx, prov) -> directionalAxisBlockUvLocked(ctx, prov,
                    (state, vertical) -> prov.models().getExistingFile(
                            Nocturnal.asResource("block/vampire/piston/" + (vertical ? "vertical" : "horizontal")))))
            .item()
            .model((ctx, prov) -> prov.withExistingParent(ctx.getName(),
                    Nocturnal.asResource("block/vampire/piston/item")))
            .build()
            .register();

    @SubscribeEvent
    public static void addValidEncasedShaftBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(AllBlockEntityTypes.ENCASED_SHAFT.get(), NocBlocks.VAMPIRE_ENCASED_SHAFT.get());
    }

    public static void register() {
        Nocturnal.LOGGER.info("Register Blocks...");
    }
}