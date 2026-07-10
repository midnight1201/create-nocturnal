package ver.faker.nocturnal.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.data.recipe.NocStandardRecipeGen;
import ver.faker.nocturnal.data.recipe.create.*;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Nocturnal.ID)
public class NocDatagen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if (!event.getMods().contains(Nocturnal.ID)) {
            return;
        }
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        if (event.includeClient()) {
            generator.addProvider(true, new NocEncasedModelGen(output, existingFileHelper));
        }
        if (event.includeServer()) {
            generator.addProvider(true, new NocStandardRecipeGen(output, lookup));
            generator.addProvider(true, new NocMixingRecipeGen(output, lookup));
            generator.addProvider(true, new NocHauntingRecipeGen(output, lookup));
            generator.addProvider(true, new NocSequencedAssemblyRecipeGen(output, lookup));
            generator.addProvider(true, new NocCrushingRecipeGen(output, lookup));
            generator.addProvider(true, new NocItemApplicationRecipeGen(output, lookup));
            generator.addProvider(true, new NocFillingRecipeGen(output, lookup));
            generator.addProvider(true, new NocDataMapProvider(output, lookup));
        }
    }
}
