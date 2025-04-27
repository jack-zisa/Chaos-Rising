package dev.creoii.chaos;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.BulletEntityType;
import dev.creoii.chaos.entity.CharacterClass;
import dev.creoii.chaos.entity.EnemyEntityType;
import dev.creoii.chaos.entity.LootDropEntityType;
import dev.creoii.chaos.item.ServerItem;
import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.util.JsonParsing;
import dev.creoii.chaos.util.Parser;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private final Map<String, Parser> schema;
    private final Map<String, Map<String, Identifiable>> data;

    public DataManager() {
        schema = new HashMap<>();
        schema.put("class", JsonParsing::parseCharacterClass);
        schema.put("item", JsonParsing::parseItem);
        schema.put("enemy", JsonParsing::parseEnemyEntityType);
        schema.put("bullet", JsonParsing::parseBulletEntityType);
        schema.put("loot_drop", JsonParsing::parseLootDropEntityType);

        data = new HashMap<>();
        for (String key : schema.keySet()) {
            data.put(key, new HashMap<>());
        }
    }

    @Nullable
    public CharacterClass getCharacterClass(String id) {
        return (CharacterClass) data.get("class").getOrDefault(id, null);
    }

    @Nullable
    public EnemyEntityType getEnemy(String id) {
        return (EnemyEntityType) data.get("enemy").getOrDefault(id, null);
    }

    @Nullable
    public BulletEntityType getBullet(String id) {
        return (BulletEntityType) data.get("bullet").getOrDefault(id, null);
    }

    @Nullable
    public LootDropEntityType getLootDrop(String id) {
        return (LootDropEntityType) data.get("loot_drop").getOrDefault(id, null);
    }

    @Nullable
    public ServerItem getItem(String id) {
        return (ServerItem) data.get("item").getOrDefault(id, null);
    }

    public void load() {
        try {
            URL baseUrl = getClass().getClassLoader().getResource("data");
            if (baseUrl == null) {
                System.out.println("[DataManager] Directory 'data/' does not exist.");
                return;
            }

            Path baseDir = Paths.get(baseUrl.toURI());

            for (Map.Entry<String, Parser> entry : schema.entrySet()) {
                String folder = entry.getKey();
                Parser parser = entry.getValue();

                Path folderPath = baseDir.resolve(folder);
                if (!Files.exists(folderPath)) {
                    System.out.println("[DataManager] Folder '" + folderPath + "' does not exist, skipping.");
                    continue;
                }

                try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, "*.json")) {
                    for (Path file : stream) {
                        try (InputStream input = Files.newInputStream(file)) {
                            JsonValue jsonValue = new JsonReader().parse(new InputStreamReader(input, StandardCharsets.UTF_8));
                            String id = com.google.common.io.Files.getNameWithoutExtension(file.getFileName().toString());

                            Identifiable obj = parser.parse(id, jsonValue);
                            data.get(folder).put(obj.id(), obj);
                        } catch (Exception e) {
                            System.out.println("[DataManager] Error parsing " + file.getFileName() + " in '/" + folder + "': " + e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[DataManager] Error loading data: " + e);
            e.printStackTrace();
        }
    }
}
