package ver.faker.nocturnal.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import ver.faker.nocturnal.register.NocItems;

import java.util.concurrent.CompletableFuture;

public class NocDataMapProvider extends DataMapProvider {

    public NocDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(NocItems.POWDERED_CARBON.get().builtInRegistryHolder(), new FurnaceFuel(1600), false);
    }
}