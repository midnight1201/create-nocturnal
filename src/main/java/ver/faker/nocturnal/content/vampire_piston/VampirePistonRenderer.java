package ver.faker.nocturnal.content.vampire_piston;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;

import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import ver.faker.nocturnal.content.NocPartialModels;

public class VampirePistonRenderer extends KineticBlockEntityRenderer<VampirePistonBlockEntity> {

    public VampirePistonRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(VampirePistonBlockEntity be, BlockState state) {
        return CachedBuffers.block(shaft(getRotationAxisOf(be)));
    }

    @Override
    protected void renderSafe(VampirePistonBlockEntity be, float partialTicks, PoseStack ms,
                              MultiBufferSource buffer, int light, int overlay) {
        BlockState state = be.getBlockState();
        renderRotatingBuffer(be, getRotatedModel(be, state), ms,
                buffer.getBuffer(RenderType.solid()), light);

        Direction facing = state.getValue(VampirePistonBlock.FACING);
        float off = be.getBitOffset(partialTicks);
        SuperByteBuffer bit = CachedBuffers.partial(NocPartialModels.VAMPIRE_PISTON_BIT, state);
        bit.center()
                .rotateToFace(facing)
                .uncenter()
                .translate(0, 0, -off)
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));
    }
}