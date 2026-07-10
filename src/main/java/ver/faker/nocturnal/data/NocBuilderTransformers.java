package ver.faker.nocturnal.data;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogwheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.content.NocSpriteShifts;

public class NocBuilderTransformers {

    public static <B extends EncasedShaftBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> bloodEncasedShaft() {
        return b -> b
                .initialProperties(SharedProperties::stone)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .loot((p, lb) -> p.dropOther(lb, AllBlocks.SHAFT.get()))
                .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(NocSpriteShifts.BLOOD_CASING)))
                .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, NocSpriteShifts.BLOOD_CASING,
                        (s, f) -> f.getAxis() != s.getValue(EncasedShaftBlock.AXIS))))
                .onRegister(block -> BlockStressValues.IMPACTS.register(block, () -> 0.0))
                .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, state ->
                        p.models().getExistingFile(Nocturnal.asResource("block/blood_encased_shaft")), true))
                .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
                .item()
                .model((c, p) -> p.getExistingFile(Nocturnal.asResource("item/blood_encased_shaft")))
                .build();
    }

    @SuppressWarnings("removal")
    public static <B extends EncasedCogwheelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> bloodEncasedCogwheel(boolean large) {
        String blockFolder = large ? "encased_large_cogwheel" : "encased_cogwheel";
        String sideTex = large
                ? "block/blood_encased_cogwheel_side_connected"
                : "block/blood_encased_cogwheel_side";

        return b -> b
                .initialProperties(SharedProperties::stone)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .loot((p, lb) -> p.dropOther(lb, large ? AllBlocks.LARGE_COGWHEEL.get() : AllBlocks.COGWHEEL.get()))
                .addLayer(() -> RenderType::cutoutMipped)
                .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, NocSpriteShifts.BLOOD_CASING,
                        (s, f) -> f.getAxis() == s.getValue(EncasedCogwheelBlock.AXIS)
                                && !s.getValue(f.getAxisDirection() == Direction.AxisDirection.POSITIVE
                                ? EncasedCogwheelBlock.TOP_SHAFT
                                : EncasedCogwheelBlock.BOTTOM_SHAFT))))
                .onRegister(block -> BlockStressValues.IMPACTS.register(block, () -> 0.0))
                .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, state -> {
                    String suffix = (state.getValue(EncasedCogwheelBlock.TOP_SHAFT) ? "_top" : "")
                            + (state.getValue(EncasedCogwheelBlock.BOTTOM_SHAFT) ? "_bottom" : "");
                    String prefix = large ? "blood_encased_large_cogwheel" : "blood_encased_cogwheel";
                    return p.models().getExistingFile(Nocturnal.asResource("block/" + prefix + suffix));
                }, false))
                .transform(EncasingRegistry.addVariantTo(large ? AllBlocks.LARGE_COGWHEEL : AllBlocks.COGWHEEL))
                .item()
                .model((c, p) -> p.getExistingFile(Nocturnal.asResource(
                        "item/" + (large ? "blood_encased_large_cogwheel" : "blood_encased_cogwheel"))))
                .build();
    }
}