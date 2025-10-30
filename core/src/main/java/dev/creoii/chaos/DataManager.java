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
import dev.creoii.chaos.util.logging.Logger;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import javax.annotation.Nullable;
import java.io.FileReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

public class DataManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Logger LOGGER = new Logger(DataManager.class.getSimpleName());
    private static final EnumMap<SchemaType, Codec<? extends Identifiable>> SCHEMA = new EnumMap<>(SchemaType.class);
    private static final EnumMap<SchemaType, Object2ObjectArrayMap<String, Identifiable>> DATA = new EnumMap<>(SchemaType.class);

    public static Object2ObjectArrayMap<String, Identifiable> getClasses() {
        return DATA.get(SchemaType.CLASS);
    }

    public static Object2ObjectArrayMap<String, Identifiable> getItems() {
        return DATA.get(SchemaType.ITEM);
    }

    public static Object2ObjectArrayMap<String, Identifiable> getEntities() {
        return DATA.get(SchemaType.ENTITY_TYPE);
    }

    public static Object2ObjectArrayMap<String, Identifiable> getLootTables() {
        return DATA.get(SchemaType.LOOT_TABLE);
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

    public static void load(Path path) {
        try {
            for (Map.Entry<SchemaType, Codec<? extends Identifiable>> entry : SCHEMA.entrySet()) {
                String folder = entry.getKey().name().toLowerCase();
                Codec<?> codec = entry.getValue();

                Path folderPath = path.resolve(folder);
                if (!Files.exists(folderPath)) {
                    LOGGER.info("Folder '" + folderPath + "' does not exist, skipping.");
                    continue;
                }

                Object2ObjectArrayMap<String, Identifiable> data = DATA.get(entry.getKey());
                try (Stream<Path> paths = Files.walk(folderPath)) {
                    paths.filter(p -> p.toString().endsWith(".json")).forEach(file -> {
                        try (Reader reader = new FileReader(file.toFile())) {
                            JsonElement jsonValue = GSON.fromJson(reader, JsonElement.class);
                            Identifiable obj = (Identifiable) codec.parse(JsonOps.INSTANCE, jsonValue).getOrThrow();
                            data.put(obj.id(), obj);
                        } catch (Exception e) {
                            LOGGER.info("Error parsing " + file.getFileName() + " in '/" + folder + "': " + e);
                        }
                    });
                }

                LOGGER.info("Loaded " + data.size() + " objects for type " + folder);
            }
        } catch (Exception e) {
            LOGGER.error("Error loading data: " + e);
        }
    }

    public static void load() {
        URL baseUrl = DataManager.class.getClassLoader().getResource("data");
        if (baseUrl == null) {
            LOGGER.error("Directory 'data/' does not exist");
            return;
        }

        Path path = null;

        try {
            path = Paths.get(baseUrl.toURI());
        } catch (URISyntaxException e) {
            LOGGER.info("Folder '" + path + "' does not exist, skipping.");
            return;
        }

        load(path);
    }

    public enum SchemaType {
        CLASS,
        ITEM,
        ENTITY_TYPE,
        LOOT_TABLE
    }

    static {
        SCHEMA.put(SchemaType.CLASS, CharacterClass.CODEC);
        SCHEMA.put(SchemaType.ITEM, Item.CODEC);
        SCHEMA.put(SchemaType.ENTITY_TYPE, EntityType.CODEC);
        SCHEMA.put(SchemaType.LOOT_TABLE, LootTable.OBJECT_CODEC);

        for (SchemaType schemaType : SCHEMA.keySet()) {
            DATA.put(schemaType, new Object2ObjectArrayMap<>());
        }
    }
}
