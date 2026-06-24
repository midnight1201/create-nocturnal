package ver.faker.nocturnal.register;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import ver.faker.nocturnal.Nocturnal;

public class NocCreativeModeTabs {

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MAIN_TAB =
            Nocturnal.REGISTRATE.defaultCreativeTab("main_tab", builder ->
                    builder
                            .title(Component.translatable("itemGroup." + Nocturnal.ID))
                            .icon(() -> new ItemStack(Items.HONEYCOMB))  // Replace with your own icon
                            .build()
            ).register();

    public static void register() {
        Nocturnal.LOGGER.info("Register Creative Tab...");
    }
}
