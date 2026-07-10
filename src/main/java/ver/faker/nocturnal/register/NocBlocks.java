package ver.faker.nocturnal.register;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogCTBehaviour;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogwheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.createmod.catnip.data.Couple;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.content.NocSpriteShifts;
import ver.faker.nocturnal.content.vampire_piston.VampirePistonBlock;
import ver.faker.nocturnal.content.vampire_piston.VampirePistonCTBehaviour;
import ver.faker.nocturnal.data.NocBuilderTransformers;

import static ver.faker.nocturnal.content.NocBlockStateGen.directionalAxisBlockUvLocked;

public class NocBlocks {

    public static final BlockEntry<CasingBlock> BLOOD_CASING = Nocturnal.REGISTRATE
            .block("blood_casing", CasingBlock::new)
            .transform(BuilderTransformers.casing(() -> NocSpriteShifts.BLOOD_CASING))
            .register();

    public static final BlockEntry<Block> BLOOD_PLANKS = Nocturnal.REGISTRATE
            .block("blood_planks", Block::new)
            .initialProperties(() -> Blocks.MANGROVE_PLANKS)
            .simpleItem()
            .register();

    public static final BlockEntry<CasingBlock> VAMPIRE_CASING = Nocturnal.REGISTRATE
            .block("vampire_casing", CasingBlock::new)
            .transform(BuilderTransformers.casing(() -> NocSpriteShifts.VAMPIRE_CASING))
            .register();

    public static final BlockEntry<VampirePistonBlock> VAMPIRE_PISTON = Nocturnal.REGISTRATE
            .block("vampire_piston", VampirePistonBlock::new)
            .initialProperties(() -> Blocks.ANDESITE)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .onRegister(block -> BlockStressValues.CAPACITIES.register(block, () -> 256.0))
            .onRegister(BlockStressValues.setGeneratorSpeed(32))
            .onRegister(CreateRegistrate.connectedTextures(VampirePistonCTBehaviour::new))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(
                    block,
                    NocSpriteShifts.VAMPIRE_PISTON_CASING_X,
                    (state, face) -> {
                        Direction.Axis shaft = block.getRotationAxis(state);
                        Direction.Axis pipe = state.getValue(VampirePistonBlock.FACING).getAxis();
                        return face.getAxis() != pipe && face.getAxis() != shaft;
                    })))
            .blockstate((ctx, prov) -> directionalAxisBlockUvLocked(ctx, prov,
                    (state, vertical) -> prov.models().getExistingFile(
                            Nocturnal.asResource("block/vampire/piston/" + (vertical ? "vertical" : "horizontal")))))
            .item()
            .model((ctx, prov) -> prov.withExistingParent(ctx.getName(),
                    Nocturnal.asResource("block/vampire/piston/item")))
            .build()
            .register();

    public static final BlockEntry<Block> MEAT_BLOCK = Nocturnal.REGISTRATE
            .block("meat_block", Block::new)
            .initialProperties(() -> Blocks.NETHER_WART_BLOCK)
            .properties(p -> p.sound(SoundType.CORAL_BLOCK))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> EYED_MEAT_BLOCK = Nocturnal.REGISTRATE
            .block("eyed_meat_block", Block::new)
            .initialProperties(() -> Blocks.NETHER_WART_BLOCK)
            .properties(p -> p.sound(SoundType.CORAL_BLOCK))
            .simpleItem()
            .register();

    //hidden from creative inventory

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

    public static final BlockEntry<EncasedShaftBlock> BLOOD_ENCASED_SHAFT = Nocturnal.REGISTRATE
            .block("blood_encased_shaft", p -> new EncasedShaftBlock(p, NocBlocks.BLOOD_CASING::get))
            .transform(NocBuilderTransformers.bloodEncasedShaft())
            .register();

    public static final BlockEntry<EncasedCogwheelBlock> BLOOD_ENCASED_COGWHEEL = Nocturnal.REGISTRATE
            .block("blood_encased_cogwheel", p -> new EncasedCogwheelBlock(p, false, NocBlocks.BLOOD_CASING::get))
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(
                    NocSpriteShifts.BLOOD_CASING,
                    Couple.create(NocSpriteShifts.BLOOD_ENCASED_COGWHEEL_SIDE,
                            NocSpriteShifts.BLOOD_ENCASED_COGWHEEL_OTHERSIDE))))
            .transform(NocBuilderTransformers.bloodEncasedCogwheel(false))
            .register();

    public static final BlockEntry<EncasedCogwheelBlock> BLOOD_ENCASED_LARGE_COGWHEEL = Nocturnal.REGISTRATE
            .block("blood_encased_large_cogwheel", p -> new EncasedCogwheelBlock(p, true, NocBlocks.BLOOD_CASING::get))
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(NocSpriteShifts.BLOOD_CASING)))
            .transform(NocBuilderTransformers.bloodEncasedCogwheel(true))
            .register();

    public static final BlockEntry<EncasedPipeBlock> VAMPIRE_ENCASED_FLUID_PIPE = Nocturnal.REGISTRATE
            .block("vampire_encased_fluid_pipe",
                    p -> new EncasedPipeBlock(p, NocBlocks.VAMPIRE_CASING::get))
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_RED))
            .transform(TagGen.axeOrPickaxe())
            .blockstate(BlockStateGen.encasedPipe())
            .onRegister(CreateRegistrate.connectedTextures(
                    () -> new EncasedCTBehaviour(NocSpriteShifts.VAMPIRE_CASING)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(
                    block, NocSpriteShifts.VAMPIRE_CASING,
                    (s, f) -> !s.getValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f)))))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .loot((p, b) -> p.dropOther(b, AllBlocks.FLUID_PIPE.get()))
            .transform(EncasingRegistry.addVariantTo(AllBlocks.FLUID_PIPE))
            .register();

    @SubscribeEvent
    public static void addValidEncasedBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(AllBlockEntityTypes.ENCASED_SHAFT.get(),
                VAMPIRE_ENCASED_SHAFT.get(), BLOOD_ENCASED_SHAFT.get());
        event.modify(AllBlockEntityTypes.ENCASED_COGWHEEL.get(),
                BLOOD_ENCASED_COGWHEEL.get());
        event.modify(AllBlockEntityTypes.ENCASED_LARGE_COGWHEEL.get(),
                BLOOD_ENCASED_LARGE_COGWHEEL.get());
        event.modify(AllBlockEntityTypes.ENCASED_FLUID_PIPE.get(),
                VAMPIRE_ENCASED_FLUID_PIPE.get());
    }

    public static void register() {
        Nocturnal.LOGGER.info("Register Blocks...");
    }
}