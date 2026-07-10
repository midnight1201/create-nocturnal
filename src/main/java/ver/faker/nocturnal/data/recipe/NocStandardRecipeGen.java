package ver.faker.nocturnal.data.recipe;

import com.simibubi.create.AllBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.ItemLike;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.register.NocBlocks;
import ver.faker.nocturnal.register.NocItems;

import java.util.concurrent.CompletableFuture;

public class NocStandardRecipeGen extends RecipeProvider {
    public NocStandardRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        packing(output, NocItems.FLESH_MASS.get(), NocBlocks.MEAT_BLOCK.get());
        unpacking(output, NocBlocks.MEAT_BLOCK.get(), NocItems.FLESH_MASS.get());

        packing(output, NocItems.FLESH_HOST.get(), NocBlocks.EYED_MEAT_BLOCK.get());
        unpacking(output, NocBlocks.EYED_MEAT_BLOCK.get(), NocItems.FLESH_HOST.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, NocBlocks.VAMPIRE_PISTON.get())
                .define('S', AllBlocks.SHAFT.get())
                .define('L', NocItems.ANIMAL_LUNG.get())
                .define('C', NocBlocks.VAMPIRE_CASING.get())
                .define('H', NocItems.FLESH_HOST.get())
                .define('P', NocItems.CARBON_SHEET.get())
                .pattern(" S ")
                .pattern("LCH")
                .pattern(" P ")
                .unlockedBy("has_vampire_casing", has(NocBlocks.VAMPIRE_CASING))
                .save(output);
    }

    private void packing(RecipeOutput output, ItemLike item, ItemLike block) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, block)
                .define('#', item)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_" + path(item), has(item))
                .save(output);
    }

    private void unpacking(RecipeOutput output, ItemLike block, ItemLike item) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item, 9)
                .requires(block)
                .unlockedBy("has_" + path(block), has(block))
                .save(output, Nocturnal.asResource(path(item) + "_from_" + path(block)));
    }

    private static String path(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath();
    }
}