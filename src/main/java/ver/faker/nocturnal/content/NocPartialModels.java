package ver.faker.nocturnal.content;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import ver.faker.nocturnal.Nocturnal;

public class NocPartialModels {
    public static final PartialModel VAMPIRE_PISTON_BIT =
            PartialModel.of(Nocturnal.asResource("block/vampire/piston/piston_bit"));

    public static final PartialModel BLOOD_BELT_COVER_X =
            PartialModel.of(Nocturnal.asResource("block/belt_cover/blood_belt_cover_x"));
    public static final PartialModel BLOOD_BELT_COVER_Z =
            PartialModel.of(Nocturnal.asResource("block/belt_cover/blood_belt_cover_z"));

    public static void init() {}
}