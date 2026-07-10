package ver.faker.nocturnal.content;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;

import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;
import ver.faker.nocturnal.Nocturnal;

public class NocSpriteShifts {

    public static final CTSpriteShiftEntry VAMPIRE_CASING =
            CTSpriteShifter.getCT(AllCTTypes.OMNIDIRECTIONAL,
                    Nocturnal.asResource("block/vampire_casing"),
                    Nocturnal.asResource("block/vampire_casing_connected"));

    public static final CTSpriteShiftEntry BLOOD_CASING =
            CTSpriteShifter.getCT(AllCTTypes.OMNIDIRECTIONAL,
                    Nocturnal.asResource("block/blood_casing"),
                    Nocturnal.asResource("block/blood_casing_connected"));

    public static final CTSpriteShiftEntry BLOOD_ENCASED_COGWHEEL_SIDE =
            CTSpriteShifter.getCT(AllCTTypes.VERTICAL,
                    Nocturnal.asResource("block/blood_encased_cogwheel_side"),
                    Nocturnal.asResource("block/blood_encased_cogwheel_side_connected"));

    public static final CTSpriteShiftEntry BLOOD_ENCASED_COGWHEEL_OTHERSIDE =
            CTSpriteShifter.getCT(AllCTTypes.HORIZONTAL,
                    Nocturnal.asResource("block/blood_encased_cogwheel_side"),
                    Nocturnal.asResource("block/blood_encased_cogwheel_side_connected"));

    public static final SpriteShiftEntry BLOOD_BELT_CASING =
            SpriteShifter.get(
                    Create.asResource("block/belt/brass_belt_casing"),
                    Nocturnal.asResource("block/belt/blood_belt_casing"));

    public static final CTSpriteShiftEntry VAMPIRE_PISTON_CASING_X =
            CTSpriteShifter.getCT(NocCTTypes.VAMPIRE_PISTON_X,
                    Nocturnal.asResource("block/vampire/piston/vampire_piston_top"),
                    Nocturnal.asResource("block/vampire/piston/vampire_piston_top"));

    public static final CTSpriteShiftEntry VAMPIRE_PISTON_CASING_Z =
            CTSpriteShifter.getCT(NocCTTypes.VAMPIRE_PISTON_Z,
                    Nocturnal.asResource("block/vampire/piston/vampire_piston_top"),
                    Nocturnal.asResource("block/vampire/piston/vampire_piston_top"));

    public static final CTSpriteShiftEntry VAMPIRE_PISTON_CASING_X_VERT =
            CTSpriteShifter.getCT(NocCTTypes.VAMPIRE_PISTON_X_VERT,
                    Nocturnal.asResource("block/vampire/piston/vampire_piston_top"),
                    Nocturnal.asResource("block/vampire/piston/vampire_piston_top"));

    public static final CTSpriteShiftEntry VAMPIRE_PISTON_CASING_Z_VERT =
            CTSpriteShifter.getCT(NocCTTypes.VAMPIRE_PISTON_Z_VERT,
                    Nocturnal.asResource("block/vampire/piston/vampire_piston_top"),
                    Nocturnal.asResource("block/vampire/piston/vampire_piston_top"));
}