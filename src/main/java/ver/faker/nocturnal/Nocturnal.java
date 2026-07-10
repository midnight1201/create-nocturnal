package ver.faker.nocturnal;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ver.faker.nocturnal.content.NocPartialModels;
import ver.faker.nocturnal.register.NocBlockEntities;
import ver.faker.nocturnal.register.NocBlocks;
import ver.faker.nocturnal.register.NocCreativeModeTabs;
import ver.faker.nocturnal.register.NocItems;

@Mod(Nocturnal.ID)
public class Nocturnal {
    public static final String ID = "create_nocturnal";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID)
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    public Nocturnal(IEventBus modBus) {
        REGISTRATE.registerEventListeners(modBus);

        NocCreativeModeTabs.register(modBus);
        REGISTRATE.setCreativeTab(NocCreativeModeTabs.MAIN_TAB);
        NocBlocks.register();
        NocItems.register();
        NocBlockEntities.register();

        modBus.addListener(this::onCommonSetup);
        modBus.addListener(this::onClientSetup);
        modBus.addListener(NocBlocks::addValidEncasedBlocks);
        modBus.addListener(NocBlockEntities::registerCapabilities);

        Nocturnal.REGISTRATE.addRawLang("itemGroup.create_nocturnal", "Create: Nocturnal");
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Common setup...");
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Client setup...");
        NocPartialModels.init();
    }
}
