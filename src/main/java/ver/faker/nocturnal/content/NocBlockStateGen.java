package ver.faker.nocturnal.content;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.function.BiFunction;

public class NocBlockStateGen {

    public static <T extends DirectionalAxisKineticBlock> void directionalAxisBlockUvLocked(
            DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
            BiFunction<BlockState, Boolean, ModelFile> modelFunc) {
        prov.getVariantBuilder(ctx.getEntry())
                .forAllStates(state -> {
                    boolean alongFirst = state.getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
                    Direction direction = state.getValue(DirectionalAxisKineticBlock.FACING);
                    boolean vertical = direction.getAxis().isHorizontal()
                            && (direction.getAxis() == Direction.Axis.X) == alongFirst;
                    int xRot = direction == Direction.DOWN ? 270 : direction == Direction.UP ? 90 : 0;
                    int yRot = direction.getAxis().isVertical() ? (alongFirst ? 0 : 90)
                            : (int) direction.toYRot();
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state, vertical))
                            .rotationX(xRot)
                            .rotationY(yRot)
                            .uvLock(true)
                            .build();
                });
    }
}
