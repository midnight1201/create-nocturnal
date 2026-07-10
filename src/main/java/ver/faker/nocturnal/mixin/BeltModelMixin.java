package ver.faker.nocturnal.mixin;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltModel;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ver.faker.nocturnal.content.NocBeltModelProperties;
import ver.faker.nocturnal.content.NocPartialModels;
import ver.faker.nocturnal.content.NocSpriteShifts;

@Mixin(BeltModel.class)
public abstract class BeltModelMixin {

    @Redirect(
            method = "getQuads",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/content/kinetics/belt/BeltModel;SPRITE_SHIFT:Lnet/createmod/catnip/render/SpriteShiftEntry;",
                    opcode = org.objectweb.asm.Opcodes.GETSTATIC,
                    remap = false
            )
    )
    private SpriteShiftEntry nocturnal$beltCasingShift(BlockState state, net.minecraft.core.Direction side,
                                                       net.minecraft.util.RandomSource rand,
                                                       ModelData extraData,
                                                       net.minecraft.client.renderer.RenderType renderType) {
        if (extraData.has(NocBeltModelProperties.BLOOD_CASING)
                && Boolean.TRUE.equals(extraData.get(NocBeltModelProperties.BLOOD_CASING))) {
            return NocSpriteShifts.BLOOD_BELT_CASING;
        }
        return AllSpriteShifts.ANDESIDE_BELT_CASING;
    }

    @Redirect(
            method = "getQuads",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/AllPartialModels;ANDESITE_BELT_COVER_X:Ldev/engine_room/flywheel/lib/model/baked/PartialModel;",
                    opcode = org.objectweb.asm.Opcodes.GETSTATIC,
                    remap = false
            )
    )
    private PartialModel nocturnal$bloodCoverX(BlockState state, Direction side, RandomSource rand,
                                               ModelData extraData, RenderType renderType) {
        if (extraData.has(NocBeltModelProperties.BLOOD_CASING)
                && Boolean.TRUE.equals(extraData.get(NocBeltModelProperties.BLOOD_CASING))) {
            return NocPartialModels.BLOOD_BELT_COVER_X;
        }
        return AllPartialModels.ANDESITE_BELT_COVER_X;
    }

    @Redirect(
            method = "getQuads",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/AllPartialModels;ANDESITE_BELT_COVER_Z:Ldev/engine_room/flywheel/lib/model/baked/PartialModel;",
                    opcode = org.objectweb.asm.Opcodes.GETSTATIC,
                    remap = false
            )
    )
    private PartialModel nocturnal$bloodCoverZ(BlockState state, Direction side, RandomSource rand,
                                               ModelData extraData, RenderType renderType) {
        if (extraData.has(NocBeltModelProperties.BLOOD_CASING)
                && Boolean.TRUE.equals(extraData.get(NocBeltModelProperties.BLOOD_CASING))) {
            return NocPartialModels.BLOOD_BELT_COVER_Z;
        }
        return AllPartialModels.ANDESITE_BELT_COVER_Z;
    }

    @Inject(method = "getParticleIcon", at = @At("HEAD"), cancellable = true)
    private void nocturnal$bloodParticle(ModelData data, CallbackInfoReturnable<TextureAtlasSprite> cir) {
        if (!data.has(BeltModel.CASING_PROPERTY)) {
            return;
        }
        BeltBlockEntity.CasingType type = data.get(BeltModel.CASING_PROPERTY);
        if (type != BeltBlockEntity.CasingType.ANDESITE) {
            return;
        }
        if (!data.has(NocBeltModelProperties.BLOOD_CASING)
                || !Boolean.TRUE.equals(data.get(NocBeltModelProperties.BLOOD_CASING))) {
            return;
        }
        cir.setReturnValue(NocSpriteShifts.BLOOD_BELT_CASING.getTarget());
    }
}