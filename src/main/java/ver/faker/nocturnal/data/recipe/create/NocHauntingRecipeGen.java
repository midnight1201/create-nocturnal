package ver.faker.nocturnal.data.recipe.create;

import com.simibubi.create.api.data.recipe.HauntingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.register.NocItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class NocHauntingRecipeGen extends HauntingRecipeGen {
    public NocHauntingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Nocturnal.ID);
    }

    GeneratedRecipe

    FLESH_HOST = convert(NocItems.FLESH_MASS.get(), NocItems.FLESH_HOST.get())
    ;
}
