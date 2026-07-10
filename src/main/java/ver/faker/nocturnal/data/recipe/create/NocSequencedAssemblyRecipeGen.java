package ver.faker.nocturnal.data.recipe.create;

import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import net.midnight.hemodynamics.HemoFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.register.NocItems;
import ver.faker.nocturnal.register.NocTags;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class NocSequencedAssemblyRecipeGen extends SequencedAssemblyRecipeGen {
    public NocSequencedAssemblyRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Nocturnal.ID);
    }

    GeneratedRecipe

    CARBON_SHEET = create("carbon_sheet", b -> b
            .require(NocTags.Items.DUSTS_COAL)
            .transitionTo(NocItems.UNPROCESSED_CARBON_SHEET.get())
            .addOutput(NocItems.CARBON_SHEET.get(), 1)
            .loops(1)
            .addStep(FillingRecipe::new, rb -> rb.require(HemoFluids.BLOOD.get(), 500))
            .addStep(PressingRecipe::new, rb -> rb)
            .addStep(PressingRecipe::new, rb -> rb))

    ;
}
