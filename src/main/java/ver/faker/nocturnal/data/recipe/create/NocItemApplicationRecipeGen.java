package ver.faker.nocturnal.data.recipe.create;

import com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.register.NocBlocks;
import ver.faker.nocturnal.register.NocItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class NocItemApplicationRecipeGen extends ItemApplicationRecipeGen {

    public NocItemApplicationRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Nocturnal.ID);
    }

    GeneratedRecipe
            BLOOD_CASING = create("blood_casing", b -> b
            .require(NocBlocks.BLOOD_PLANKS.get())
            .require(NocItems.SANGUINE_STEEL_INGOT.get())
            .output(NocBlocks.BLOOD_CASING.get())),

            VAMPIRE_CASING = create("vampire_casing", b -> b
            .require(NocBlocks.BLOOD_CASING.get())
            .require(NocItems.CARBON_SHEET.get())
            .output(NocBlocks.VAMPIRE_CASING.get()));
}