package ver.faker.nocturnal.data;

import com.google.gson.JsonParser;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import ver.faker.nocturnal.Nocturnal;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class NocEncasedModelGen implements DataProvider {

    private static final String[] MODELS = {
            "block/blood_encased_shaft",
            "item/blood_encased_shaft",
            "block/blood_encased_cogwheel",
            "block/blood_encased_cogwheel_top",
            "block/blood_encased_cogwheel_bottom",
            "block/blood_encased_cogwheel_top_bottom",
            "item/blood_encased_cogwheel",
            "block/blood_encased_large_cogwheel",
            "block/blood_encased_large_cogwheel_top",
            "block/blood_encased_large_cogwheel_bottom",
            "block/blood_encased_large_cogwheel_top_bottom",
            "item/blood_encased_large_cogwheel",
    };

    private final PackOutput output;
    private final PackOutput.PathProvider models;

    private static final ExistingFileHelper.ResourceType MODEL_TYPE =
            new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

    public NocEncasedModelGen(PackOutput output, ExistingFileHelper helper) {
        this.output = output;
        this.models = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
        for (String path : MODELS) {
            helper.trackGenerated(Nocturnal.asResource(path), MODEL_TYPE);
        }
    }
    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return CompletableFuture.allOf(
                // --- shaft ---
                save(cache, "block/blood_encased_shaft",
                        encasedShaft("create:block/encased_shaft/block", "blood_casing", "blood_gearbox")),
                save(cache, "item/blood_encased_shaft",
                        encasedShaft("create:block/encased_shaft/item", "blood_casing", "blood_gearbox")),

                // --- small cogwheel ---
                save(cache, "block/blood_encased_cogwheel",
                        encasedCogwheel("create:block/encased_cogwheel/block", "blood_casing", "blood_gearbox",
                                "stripped_blood_log_top", "blood_encased_cogwheel_side")),
                save(cache, "block/blood_encased_cogwheel_top",
                        encasedCogwheel("create:block/encased_cogwheel/block_top", "blood_casing", "blood_gearbox",
                                "stripped_blood_log_top", "blood_encased_cogwheel_side")),
                save(cache, "block/blood_encased_cogwheel_bottom",
                        encasedCogwheel("create:block/encased_cogwheel/block_bottom", "blood_casing", "blood_gearbox",
                                "stripped_blood_log_top", "blood_encased_cogwheel_side")),
                save(cache, "block/blood_encased_cogwheel_top_bottom",
                        encasedCogwheel("create:block/encased_cogwheel/block_top_bottom", "blood_casing", "blood_gearbox",
                                "stripped_blood_log_top", "blood_encased_cogwheel_side")),
                save(cache, "item/blood_encased_cogwheel",
                        encasedCogwheelItem("create:block/encased_cogwheel/item", "blood_casing",
                                "stripped_blood_log_top", "blood_encased_cogwheel_side")),

                // --- large cogwheel ---
                save(cache, "block/blood_encased_large_cogwheel",
                        encasedCogwheel("create:block/encased_large_cogwheel/block", "blood_casing", "blood_gearbox",
                                "stripped_blood_log_top", "blood_encased_cogwheel_side_connected")),
                save(cache, "block/blood_encased_large_cogwheel_top",
                        encasedCogwheel("create:block/encased_large_cogwheel/block_top", "blood_casing", "blood_gearbox",
                                "stripped_blood_log_top", "blood_encased_cogwheel_side_connected")),
                save(cache, "block/blood_encased_large_cogwheel_bottom",
                        encasedCogwheel("create:block/encased_large_cogwheel/block_bottom", "blood_casing", "blood_gearbox",
                                "stripped_blood_log_top", "blood_encased_cogwheel_side_connected")),
                save(cache, "block/blood_encased_large_cogwheel_top_bottom",
                        encasedCogwheel("create:block/encased_large_cogwheel/block_top_bottom", "blood_casing", "blood_gearbox",
                                "stripped_blood_log_top", "blood_encased_cogwheel_side_connected")),
                save(cache, "item/blood_encased_large_cogwheel",
                        encasedCogwheelItem("create:block/encased_large_cogwheel/item", "blood_casing",
                                "stripped_blood_log_top", "blood_encased_cogwheel_side_connected"))
        );
    }

    // 2-line templates

    private static String encasedShaft(String parent, String casing, String opening) {
        return """
                {
                  "parent": "%s",
                  "textures": {
                    "casing": "create_nocturnal:block/%s",
                    "opening": "create_nocturnal:block/%s"
                  }
                }
                """.formatted(parent, casing, opening);
    }

    private static String encasedCogwheel(String parent, String casing, String gearbox, String logTop, String side) {
        return """
                {
                  "parent": "%s",
                  "textures": {
                    "casing": "create_nocturnal:block/%s",
                    "particle": "create_nocturnal:block/%s",
                    "4": "create_nocturnal:block/%s",
                    "1": "create_nocturnal:block/%s",
                    "side": "create_nocturnal:block/%s"
                  }
                }
                """.formatted(parent, casing, casing, gearbox, logTop, side);
    }

    private static String encasedCogwheelItem(String parent, String casing, String logTop, String side) {
        return """
                {
                  "parent": "%s",
                  "textures": {
                    "casing": "create_nocturnal:block/%s",
                    "particle": "create_nocturnal:block/%s",
                    "1": "create_nocturnal:block/%s",
                    "side": "create_nocturnal:block/%s"
                  }
                }
                """.formatted(parent, casing, casing, logTop, side);
    }

    private CompletableFuture<?> save(CachedOutput cache, String path, String json) {
        Path file = models.json(Nocturnal.asResource(path));
        return DataProvider.saveStable(cache, JsonParser.parseString(json), file);
    }

    @Override
    public String getName() {
        return "Nocturnal Encased Models";
    }
}