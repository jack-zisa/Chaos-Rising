package dev.creoii.chaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.character.CharacterClass;
import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.util.stat.Stats;

import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private final Main main;
    private final Json json;
    private final Map<String, Parser> schema;
    private final Map<String, Map<String, Identifiable>> data;

    public DataManager(Main main) {
        this.main = main;
        json = new Json();

        json.setSerializer(Stats.class, new Stats.Serializer());
        json.setSerializer(CharacterClass.class, new CharacterClass.Serializer());
        json.setSerializer(Item.class, new Item.Serializer());
        json.setSerializer(EnemyEntity.class, new EnemyEntity.Serializer());
        json.setSerializer(BulletEntity.class, new BulletEntity.Serializer());

        schema = new HashMap<>();
        schema.put("class", fileHandle -> json.fromJson(CharacterClass.class, fileHandle));
        schema.put("item", fileHandle -> json.fromJson(Item.class, fileHandle));
        schema.put("enemy", fileHandle -> json.fromJson(EnemyEntity.class, fileHandle));
        schema.put("bullet", fileHandle -> json.fromJson(BulletEntity.class, fileHandle));

        data = new HashMap<>();
        for (String key : schema.keySet()) {
            data.put(key, new HashMap<>());
        }
    }

    public Json getJson() {
        return json;
    }

    public CharacterClass getCharacterClass(String id) {
        return (CharacterClass) data.get("class").get(id);
    }

    public EnemyEntity getEnemy(String id) {
        return (EnemyEntity) data.get("enemy").get(id);
    }

    public BulletEntity getBullet(String id) {
        return (BulletEntity) data.get("bullet").get(id);
    }

    public Item getItem(String id) {
        return (Item) data.get("item").get(id);
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
                    obj.onLoad(main);
                    data.get(folder).put(obj.id(), obj);
                } catch (Exception e) {
                    Gdx.app.error(DataManager.class.getSimpleName(), "Error parsing " + file.name() + " in '/" + folder + "': " + e.getMessage());
                }
            }
        }
    }

    @FunctionalInterface
    interface Parser {
        Identifiable parse(FileHandle jsonFile);
    }

    public interface Identifiable {
        String id();

        default void onLoad(Main main) {}
    }
}
