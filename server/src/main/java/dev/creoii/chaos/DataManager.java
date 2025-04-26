package dev.creoii.chaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import dev.creoii.chaos.data.JsonParsing;
import dev.creoii.chaos.entity.BulletEntityType;
import dev.creoii.chaos.entity.EnemyEntityType;
import dev.creoii.chaos.entity.LootDropEntityType;
import dev.creoii.chaos.entity.character.CharacterClass;
import dev.creoii.chaos.item.ServerItem;
import dev.creoii.chaos.util.Identifiable;
import dev.creoii.chaos.util.Parser;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private final Map<String, Parser> schema;
    private final Map<String, Map<String, Identifiable>> data;

    public DataManager() {
        schema = new HashMap<>();
        schema.put("class", fileHandle -> JsonParsing.parseCharacterClass(fileHandle.nameWithoutExtension(), new JsonReader().parse(fileHandle)));
        schema.put("item", fileHandle -> JsonParsing.parseItem(fileHandle.nameWithoutExtension(), new JsonReader().parse(fileHandle)));
        schema.put("enemy", fileHandle -> JsonParsing.parseEnemyEntityType(fileHandle.nameWithoutExtension(), new JsonReader().parse(fileHandle)));
        schema.put("bullet", fileHandle -> JsonParsing.parseBulletEntityType(fileHandle.nameWithoutExtension(), new JsonReader().parse(fileHandle)));
        schema.put("loot_drop", fileHandle -> JsonParsing.parseLootDropEntityType(fileHandle.nameWithoutExtension(), new JsonReader().parse(fileHandle)));

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
        FileHandle baseDir = Gdx.files.internal("data");

        if (!baseDir.exists()) {
            Gdx.app.log(DataManager.class.getSimpleName(), "Directory 'data/' does not exist.");
            return;
        }

        for (Map.Entry<String, Parser> entry : schema.entrySet()) {
            String folder = entry.getKey();
            Parser parser = entry.getValue();

            FileHandle folderHandle = baseDir.child(folder);
            if (!folderHandle.exists()) {
                Gdx.app.log(DataManager.class.getSimpleName(), "Folder '" + folderHandle.path() + "' does not exist, skipping.");
                continue;
            }

            for (FileHandle file : folderHandle.list("json")) {
                try {
                    Identifiable obj = parser.parse(file);
                    data.get(folder).put(obj.id(), obj);
                } catch (Exception e) {
                    Gdx.app.error(DataManager.class.getSimpleName(), "Error parsing " + file.name() + " in '/" + folder + "': " + e);
                }
            }
        }
    }
}
