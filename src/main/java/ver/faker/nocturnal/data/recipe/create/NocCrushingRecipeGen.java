package ver.faker.nocturnal.data.recipe.create;

import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.register.NocItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class NocCrushingRecipeGen extends CrushingRecipeGen {
    public NocCrushingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Nocturnal.ID);
    }

    GeneratedRecipe

    POWDERED_CARBON = create("powdered_carbon", b -> b
            .require(ItemTags.COALS)
            .output(NocItems.POWDERED_CARBON.get(), 1))
    ;

}
