package ver.faker.nocturnal.register;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.midnight.hemodynamics.HemoFluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import ver.faker.nocturnal.Nocturnal;

import java.util.List;

public class NocCreativeModeTabs {

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MAIN_TAB =
            Nocturnal.REGISTRATE.defaultCreativeTab("main_tab", builder ->
                    builder
                            .title(Component.translatable("itemGroup." + Nocturnal.ID))
                            .icon(() -> new ItemStack(NocBlocks.VAMPIRE_PISTON))
                            .build()
            ).register();

    private static final List<ItemProviderEntry<?, ?>> HIDDEN = List.of(
            NocBlocks.VAMPIRE_ENCASED_SHAFT,
            NocBlocks.BLOOD_ENCASED_SHAFT,
            NocBlocks.BLOOD_ENCASED_COGWHEEL,
            NocBlocks.BLOOD_ENCASED_LARGE_COGWHEEL,
            NocItems.UNPROCESSED_CARBON_SHEET
    );

    public static void register(IEventBus modBus) {
        modBus.addListener(NocCreativeModeTabs::customizeTab);
        Nocturnal.LOGGER.info("Register Creative Tab...");
    }

    private static void customizeTab(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(MAIN_TAB.getKey())) return;

        for (ItemProviderEntry<?, ?> entry : HIDDEN) {
            event.remove(
                    new ItemStack(entry.asItem()),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }

        HemoFluids.BLOOD.getBucket().ifPresent(bucket ->
                event.accept(
                        new ItemStack(bucket),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
                )
        );
    }
}