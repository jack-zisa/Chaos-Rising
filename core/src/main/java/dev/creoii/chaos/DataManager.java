package dev.creoii.chaos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.loot.LootTable;
import dev.creoii.chaos.util.Identifiable;

import javax.annotation.Nullable;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Codec<? extends Identifiable>> SCHEMA = new HashMap<>();
    private static final Map<String, Map<String, Identifiable>> DATA = new HashMap<>();

    public DataManager() {
        SCHEMA.put("class", CharacterClass.CODEC);
        SCHEMA.put("item", Item.CODEC);
        SCHEMA.put("entity", EntityType.CODEC);
        SCHEMA.put("loot_table", LootTable.CODEC);

        for (String key : SCHEMA.keySet()) {
            DATA.put(key, new HashMap<>());
        }
    }

    public static Map<String, Identifiable> getClasses() {
        return DATA.get("class");
    }

    public static Map<String, Identifiable> getItems() {
        return DATA.get("item");
    }

    public static Map<String, Identifiable> getEntities() {
        return DATA.get("entity");
    }

    public static Map<String, Identifiable> getLootTables() {
        return DATA.get("loot_table");
    }

    @Nullable
    public static CharacterClass getCharacterClass(String id) {
        return (CharacterClass) getClasses().getOrDefault(id, null);
    }

    @Nullable
    public static EnemyEntityType getEnemy(String id) {
        return (EnemyEntityType) getEntities().getOrDefault(id, null);
    }

    @Nullable
    public static BulletEntityType getBullet(String id) {
        return (BulletEntityType) getEntities().getOrDefault(id, null);
    }

    @Nullable
    public static LootDropEntityType getLootDrop(String id) {
        return (LootDropEntityType) getEntities().getOrDefault(id, null);
    }

    @Nullable
    public static Item getItem(String id) {
        return (Item) getItems().getOrDefault(id, null);
    }

    @Nullable
    public static LootTable getLootTable(String id) {
        return (LootTable) getLootTables().getOrDefault(id, null);
    }

    public void load(Path path) {
        try {
            for (Map.Entry<String, Codec<? extends Identifiable>> entry : SCHEMA.entrySet()) {
                String folder = entry.getKey();
                Codec<?> codec = entry.getValue();

                Path folderPath = path.resolve(folder);
                if (!Files.exists(folderPath)) {
                    System.out.println("[DataManager] Folder '" + folderPath + "' does not exist, skipping.");
                    continue;
                }

                Map<String, Identifiable> data = DATA.get(folder);
                try (Stream<Path> paths = Files.walk(folderPath)) {
                    paths.filter(p -> p.toString().endsWith(".json")).forEach(file -> {
                        try (Reader reader = new FileReader(file.toFile())) {
                            JsonElement jsonValue = GSON.fromJson(reader, JsonElement.class);
                            Identifiable obj = (Identifiable) codec.parse(JsonOps.INSTANCE, jsonValue).getOrThrow();
                            data.put(obj.id(), obj);
                        } catch (Exception e) {
                            System.out.println("[DataManager] Error parsing " + file.getFileName() + " in '/" + folder + "': " + e);
                        }
                    });
                }

                System.out.println("[DataManager] Loaded " + data.size() + " objects for type " + folder);
            }
        } catch (Exception e) {
            System.out.println("[DataManager] Error loading data: " + e);
            e.printStackTrace();
        }
    }
}
