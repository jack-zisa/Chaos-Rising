package dev.creoii.chaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.character.CharacterClass;
import dev.creoii.chaos.util.stat.Stats;

import java.util.HashMap;
import java.util.Map;

public class DataManager {
    public static final String DEFAULT_SPRITE_PATH = "missing";
    private final Json json;
    private final Map<String, Parser> schema;
    private final Map<String, Map<String, Identifiable>> data;

    public DataManager() {
        json = new Json();

        json.setSerializer(Stats.class, new Stats.Serializer());
        json.setSerializer(CharacterClass.class, new CharacterClass.Serializer());
        json.setSerializer(EnemyEntity.class, new EnemyEntity.Serializer());

        schema = new HashMap<>();
        schema.put("class", fileHandle -> json.fromJson(CharacterClass.class, fileHandle));
        schema.put("enemy", fileHandle -> json.fromJson(EnemyEntity.class, fileHandle));
        //schema.put("bullet", fileHandle -> json.fromJson(Bullet.class, fileHandle));
        //schema.put("item", fileHandle -> json.fromJson(Item.class, fileHandle));

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

    /*public Bullet getBullet(String id) {
        return (Bullet) data.get("bullet").get(id);
    }

    public Item getItem(String id) {
        return (Item) data.get("item").get(id);
    }*/

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
                    data.get(folder).put(obj.getId(), obj);
                } catch (Exception e) {
                    Gdx.app.error(DataManager.class.getSimpleName(), "Error parsing " + file.name() + " in " + folder + ": " + e.getMessage());
                }
            }
        }
    }

    @FunctionalInterface
    interface Parser {
        Identifiable parse(FileHandle jsonFile);
    }

    public interface Identifiable {
        String getId();
    }
}
