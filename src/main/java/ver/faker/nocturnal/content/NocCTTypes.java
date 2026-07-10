package ver.faker.nocturnal.content;

import com.simibubi.create.foundation.block.connected.*;
import net.minecraft.resources.ResourceLocation;
import ver.faker.nocturnal.Nocturnal;

public enum NocCTTypes implements CTType {
    VAMPIRE_PISTON_X(4, ConnectedTextureBehaviour.ContextRequirement.builder().horizontal().build()) {
        @Override
        public int getTextureIndex(ConnectedTextureBehaviour.CTContext c) {
            if (!c.left && !c.right) return 12;
            if (!c.right) return 13;
            if (!c.left) return 15;
            return 14;
        }
    },
    VAMPIRE_PISTON_Z(4, ConnectedTextureBehaviour.ContextRequirement.builder().horizontal().build()) {
        @Override
        public int getTextureIndex(ConnectedTextureBehaviour.CTContext c) {
            if (!c.left && !c.right) return 12;
            if (!c.right) return 0;
            if (!c.left) return 8;
            return 4;
        }
    },

    VAMPIRE_PISTON_X_VERT(4, ConnectedTextureBehaviour.ContextRequirement.builder().vertical().build()) {
        @Override
        public int getTextureIndex(ConnectedTextureBehaviour.CTContext c) {
            if (!c.up && !c.down) return 12;
            if (!c.down) return 15;
            if (!c.up) return 13;
            return 14;
        }
    },
    VAMPIRE_PISTON_Z_VERT(4, ConnectedTextureBehaviour.ContextRequirement.builder().vertical().build()) {
        @Override
        public int getTextureIndex(ConnectedTextureBehaviour.CTContext c) {
            if (!c.up && !c.down) return 12;
            if (!c.down) return 0;
            if (!c.up) return 8;
            return 4;
        }
    };

    private final ResourceLocation id;
    private final int sheetSize;
    private final ConnectedTextureBehaviour.ContextRequirement contextRequirement;

    NocCTTypes(int sheetSize, ConnectedTextureBehaviour.ContextRequirement req) {
        this.id = Nocturnal.asResource(name().toLowerCase());
        this.sheetSize = sheetSize;
        this.contextRequirement = req;
        CTTypeRegistry.register(this);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public int getSheetSize() {
        return sheetSize;
    }

    @Override
    public ConnectedTextureBehaviour.ContextRequirement getContextRequirement() {
        return contextRequirement;
    }
}