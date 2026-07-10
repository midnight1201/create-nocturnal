package ver.faker.nocturnal.register;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;

public class NocTags {

    public static class Items {
        public static final TagKey<Item> DUSTS = Tags.Items.DUSTS;
        public static final TagKey<Item> DUSTS_COAL = cItem("dusts/coal");
        public static final TagKey<Item> PLATES = cItem("plates");
        public static final TagKey<Item> PLATES_CARBON = cItem("plates/carbon");

        private static TagKey<Item> cItem(String path) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
        }
    }

    public static void init() {
    }
}