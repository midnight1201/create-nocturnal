package ver.faker.nocturnal.register;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import ver.faker.nocturnal.Nocturnal;

public class NocItems {

    public static final ItemEntry<Item> ANIMAL_LUNG =
            Nocturnal.REGISTRATE.item("animal_lung", Item::new).register();
    public static final ItemEntry<Item> FLESH_MASS =
            Nocturnal.REGISTRATE.item("flesh_mass", Item::new).register();
    public static final ItemEntry<Item> FLESH_HOST =
            Nocturnal.REGISTRATE.item("flesh_host", Item::new).register();

    public static final ItemEntry<Item> SANGUINE_STEEL_INGOT =
            Nocturnal.REGISTRATE.item("sanguine_steel_ingot", Item::new)
                    .tag(Tags.Items.INGOTS)
                    .register();

    public static final ItemEntry<Item> CARBON_SHEET =
            Nocturnal.REGISTRATE.item("carbon_sheet", Item::new)
                    .tag(NocTags.Items.PLATES, NocTags.Items.PLATES_CARBON)
                    .register();

    public static final ItemEntry<Item> POWDERED_CARBON =
            Nocturnal.REGISTRATE.item("powdered_carbon", Item::new)
                    .tag(NocTags.Items.DUSTS, NocTags.Items.DUSTS_COAL)
                    .register();

    //hidden
    public static final ItemEntry<SequencedAssemblyItem>
            UNPROCESSED_CARBON_SHEET = sequencedIngredient("unprocessed_carbon_sheet");

    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
        return Nocturnal.REGISTRATE.item(name, SequencedAssemblyItem::new)
                .register();
    }

    public static void register() {
        Nocturnal.LOGGER.info("Register Items...");
    }
}
