package ver.faker.nocturnal.content;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;

import ver.faker.nocturnal.Nocturnal;

public class NocSpriteShifts {
    public static final CTSpriteShiftEntry VAMPIRE_CASING =
            CTSpriteShifter.getCT(AllCTTypes.OMNIDIRECTIONAL,
                    Nocturnal.asResource("block/vampire_casing"),
                    Nocturnal.asResource("block/vampire_casing_connected"));
}