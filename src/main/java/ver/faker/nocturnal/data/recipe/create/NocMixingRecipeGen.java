package ver.faker.nocturnal.data.recipe.create;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import net.midnight.hemodynamics.api.BloodFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.common.Tags;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.register.NocItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class NocMixingRecipeGen extends MixingRecipeGen {
    public NocMixingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Nocturnal.ID);
    }

    GeneratedRecipe
        FLESH_MASS = create("flesh_mass", b -> b
            .require(Tags.Items.FOODS_RAW_MEAT)
            .require(Tags.Items.FOODS_RAW_MEAT)
            .require((FlowingFluid) BloodFluids.source(), 250)
            .output(NocItems.FLESH_MASS.get())),

        SANGUINE_STEEL_INGOT = create("sanguine_steel_ingot", b -> b
            .require(Items.IRON_INGOT)
            .require(Items.IRON_INGOT)
            .require(Items.IRON_INGOT)
            .require(ItemTags.COALS)
            .require((FlowingFluid) BloodFluids.source(), 250)
            .output(NocItems.SANGUINE_STEEL_INGOT.get(), 3)
            .requiresHeat(HeatCondition.HEATED))

        ;

}
