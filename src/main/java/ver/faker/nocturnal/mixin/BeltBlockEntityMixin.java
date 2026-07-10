package ver.faker.nocturnal.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltModel;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ver.faker.nocturnal.content.BeltBlockEntityAccess;
import ver.faker.nocturnal.content.NocBeltModelProperties;
import ver.faker.nocturnal.register.NocBlocks;

import java.util.Objects;

@Mixin(BeltBlockEntity.class)
public class BeltBlockEntityMixin implements BeltBlockEntityAccess {

    @Unique
    private boolean nocturnal$bloodCasing;

    @Unique
    private BeltBlockEntity create_nocturnal$self() {
        return (BeltBlockEntity) (Object) this;
    }

    @Override
    public void nocturnal$setBloodCasing() {
        BeltBlockEntity be = create_nocturnal$self();
        if (nocturnal$bloodCasing && be.casing == BeltBlockEntity.CasingType.ANDESITE) {
            return;
        }

        BlockState blockState = be.getBlockState();
        boolean shouldHaveCasing = true;

        if (Objects.requireNonNull(be.getLevel()).isClientSide()) {
            be.casing = BeltBlockEntity.CasingType.ANDESITE;
            nocturnal$bloodCasing = true;
            be.getLevel().setBlock(be.getBlockPos(), blockState.setValue(BeltBlock.CASING, shouldHaveCasing), 0);
            be.requestModelDataUpdate();
            be.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 16);
            return;
        }

        if (be.casing != BeltBlockEntity.CasingType.NONE) {
            BlockState oldCasing = nocturnal$bloodCasing
                    ? NocBlocks.BLOOD_CASING.getDefaultState()
                    : (be.casing == BeltBlockEntity.CasingType.ANDESITE
                    ? AllBlocks.ANDESITE_CASING.getDefaultState()
                    : AllBlocks.BRASS_CASING.getDefaultState());
            be.getLevel().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, be.getBlockPos(), Block.getId(oldCasing));
        }

        if (blockState.getValue(BeltBlock.CASING) != shouldHaveCasing) {
            KineticBlockEntity.switchToBlockState(be.getLevel(), be.getBlockPos(),
                    blockState.setValue(BeltBlock.CASING, shouldHaveCasing));
        }

        be.casing = BeltBlockEntity.CasingType.ANDESITE;
        nocturnal$bloodCasing = true;
        be.setChanged();
        be.sendData();
    }

    @Override
    public boolean nocturnal$isBloodCasing() {
        return nocturnal$bloodCasing;
    }

    @Inject(method = "getModelData", at = @At("RETURN"), cancellable = true)
    private void nocturnal$addBloodModelData(CallbackInfoReturnable<ModelData> cir) {
        BeltBlockEntity be = create_nocturnal$self();
        cir.setReturnValue(ModelData.builder()
                .with(BeltModel.CASING_PROPERTY, be.casing)
                .with(BeltModel.COVER_PROPERTY, be.covered)
                .with(NocBeltModelProperties.BLOOD_CASING, nocturnal$bloodCasing)
                .build());
    }

    @Inject(method = "setCasingType", at = @At("HEAD"), cancellable = true)
    private void nocturnal$onSetCasingType(BeltBlockEntity.CasingType type, CallbackInfo ci) {
        if (!nocturnal$bloodCasing) {
            return;
        }
        BeltBlockEntity be = create_nocturnal$self();

        if (type == BeltBlockEntity.CasingType.ANDESITE && type == be.casing) {
            nocturnal$bloodCasing = false;
            if (be.getLevel() != null && be.getLevel().isClientSide()) {
                be.requestModelDataUpdate();
                be.getLevel().sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 16);
            } else if (be.getLevel() != null) {
                be.setChanged();
                be.sendData();
            }
            ci.cancel();
            return;
        }

        if (type != be.casing) {
            nocturnal$bloodCasing = false;
        }
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void nocturnal$writeBlood(CompoundTag compound, HolderLookup.Provider registries,
                                      boolean clientPacket, CallbackInfo ci) {
        compound.putBoolean("BloodCasing", nocturnal$bloodCasing);
    }
    @Inject(
            method = "read",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/createmod/catnip/nbt/NBTHelper;readEnum(Lnet/minecraft/nbt/CompoundTag;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Enum;",
                    shift = At.Shift.AFTER,
                    remap = false
            )
    )
    private void nocturnal$readBloodAfterCasing(CompoundTag compound, HolderLookup.Provider registries,
                                                boolean clientPacket, CallbackInfo ci) {
        if (compound.contains("BloodCasing")) {
            nocturnal$bloodCasing = compound.getBoolean("BloodCasing");
        }
        BeltBlockEntity be = create_nocturnal$self();
        if (nocturnal$bloodCasing && be.getLevel() != null && be.getLevel().isClientSide()) {
            be.requestModelDataUpdate();
        }
    }
}