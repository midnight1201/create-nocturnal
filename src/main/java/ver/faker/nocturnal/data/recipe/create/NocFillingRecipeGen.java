package ver.faker.nocturnal.data.recipe.create;

import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import net.midnight.hemodynamics.HemoFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.register.NocBlocks;

import java.util.concurrent.CompletableFuture;

public class NocFillingRecipeGen extends FillingRecipeGen {
    public NocFillingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Nocturnal.ID);
    }

    GeneratedRecipe

    BLOOD_PLANKS = create("blood_planks", b -> b
            .require(ItemTags.PLANKS)
            .require(HemoFluids.BLOOD.getSource(), 250)
            .output(NocBlocks.BLOOD_PLANKS.get()));

}
