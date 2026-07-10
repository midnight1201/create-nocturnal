package ver.faker.nocturnal.content.vampire_piston;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.decoration.encasing.CasingConnectivity;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTType;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import ver.faker.nocturnal.content.NocCTTypes;
import ver.faker.nocturnal.content.NocSpriteShifts;

public class VampirePistonCTBehaviour extends ConnectedTextureBehaviour.Base {
    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader,
                              BlockPos pos, BlockPos otherPos, Direction face) {
        if (isBeingBlocked(state, reader, pos, otherPos, face)) return false;
        if (!(state.getBlock() instanceof VampirePistonBlock self)) return false;
        if (!(other.getBlock() instanceof VampirePistonBlock otherBlock)) return false;

        Direction.Axis shaft = self.getRotationAxis(state);
        if (shaft != otherBlock.getRotationAxis(other)) return false;

        CasingConnectivity.Entry entry = CreateClient.CASING_CONNECTIVITY.get(state);
        CasingConnectivity.Entry otherEntry = CreateClient.CASING_CONNECTIVITY.get(other);
        if (entry == null || otherEntry == null) return false;
        if (!entry.isSideValid(state, face) || !otherEntry.isSideValid(other, face)) return false;
        return entry.getCasing() == otherEntry.getCasing();
    }

    private static boolean verticalModel(BlockState state) {
        boolean alongFirst = state.getValue(VampirePistonBlock.AXIS_ALONG_FIRST_COORDINATE);
        Direction facing = state.getValue(VampirePistonBlock.FACING);
        return facing.getAxis().isHorizontal()
                && (facing.getAxis() == Direction.Axis.X) == alongFirst;
    }

    private static boolean usesVerticalContext(BlockState state) {
        return verticalModel(state);
    }

    private static boolean usesZStrip(BlockState state) {
        if (usesVerticalContext(state)) return true;
        VampirePistonBlock block = (VampirePistonBlock) state.getBlock();
        Direction.Axis shaft = block.getRotationAxis(state);
        Direction facing = state.getValue(VampirePistonBlock.FACING);
        return shaft == Direction.Axis.Z && facing.getAxis().isHorizontal();
    }

    private static boolean isFalseUpDown(BlockState state) {
        VampirePistonBlock block = (VampirePistonBlock) state.getBlock();
        return block.getRotationAxis(state) == Direction.Axis.Z
                && state.getValue(VampirePistonBlock.FACING).getAxis() == Direction.Axis.Y;
    }

    private static CTSpriteShiftEntry shiftFor(BlockState state) {
        if (usesVerticalContext(state)) {
            return NocSpriteShifts.VAMPIRE_PISTON_CASING_Z_VERT;
        }
        return usesZStrip(state)
                ? NocSpriteShifts.VAMPIRE_PISTON_CASING_Z
                : NocSpriteShifts.VAMPIRE_PISTON_CASING_X;
    }

    private static CTType typeFor(BlockState state) {
        if (usesVerticalContext(state)) {
            return NocCTTypes.VAMPIRE_PISTON_Z_VERT;
        }
        return usesZStrip(state)
                ? NocCTTypes.VAMPIRE_PISTON_Z
                : NocCTTypes.VAMPIRE_PISTON_X;
    }

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, TextureAtlasSprite sprite) {
        if (!(state.getBlock() instanceof VampirePistonBlock)) return null;
        return shiftFor(state);
    }

    @Override
    public CTType getDataType(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
        if (!(state.getBlock() instanceof VampirePistonBlock)) return null;
        return typeFor(state);
    }

    @Override
    protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos,
                                          BlockState state, Direction face) {
        Direction.Axis shaft = ((VampirePistonBlock) state.getBlock()).getRotationAxis(state);
        return shaft == Direction.Axis.Z ? Direction.SOUTH : Direction.EAST;
    }

    @Override
    protected boolean reverseUVsHorizontally(BlockState state, Direction face) {
        VampirePistonBlock block = (VampirePistonBlock) state.getBlock();
        Direction.Axis shaft = block.getRotationAxis(state);

        // false E/W: bottom face end-cap flip (keep — already good)
        if (shaft == Direction.Axis.Z && face == Direction.DOWN) {
            return true;
        }

        // false U/D: swap ends on east/west casing faces
        if (isFalseUpDown(state) && face.getAxis() == Direction.Axis.X) {
            return true;
        }

        return super.reverseUVsHorizontally(state, face);
    }

    @Override
    protected boolean reverseUVsVertically(BlockState state, Direction face) {
        if (!usesVerticalContext(state)) {
            return super.reverseUVsVertically(state, face);
        }

        Direction facing = state.getValue(VampirePistonBlock.FACING);
        return switch (facing) {
            case NORTH -> true;  // false N (good)
            case SOUTH -> true;  // false S (swap ends)
            case EAST, WEST -> true; // true E/W (swap ends)
            default -> false;
        };
    }
}