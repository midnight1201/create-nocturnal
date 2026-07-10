package ver.faker.nocturnal.mixin;

import com.simibubi.create.content.kinetics.belt.BeltBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ver.faker.nocturnal.content.BeltBlockEntityAccess;
import ver.faker.nocturnal.register.NocBlocks;

@Mixin(BeltBlock.class)
public abstract class BeltBlockMixin {

    @Inject(
            method = "useItemOn",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/AllBlocks;BRASS_CASING:Lcom/tterrag/registrate/util/entry/BlockEntry;",
                    opcode = Opcodes.GETSTATIC,
                    remap = false
            ),
            cancellable = true
    )
    private void nocturnal$applyBloodCasing(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                            Player player, InteractionHand hand, BlockHitResult hitResult,
                                            CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (!NocBlocks.BLOOD_CASING.isIn(stack)) {
            return;
        }

        BeltBlock self = (BeltBlock) (Object) this;
        self.withBlockEntityDo(level, pos, be -> ((BeltBlockEntityAccess) be).nocturnal$setBloodCasing());
        self.updateCoverProperty(level, pos, level.getBlockState(pos));

        SoundType soundType = NocBlocks.BLOOD_CASING.getDefaultState()
                .getSoundType(level, pos, player);
        level.playSound(null, pos, soundType.getPlaceSound(), SoundSource.BLOCKS,
                (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);

        cir.setReturnValue(ItemInteractionResult.SUCCESS);
    }
}