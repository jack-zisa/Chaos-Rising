package dev.creoii.chaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private final Json json;

    private final Map<String, Parser> dataSchema;
    private final Map<String, Map<String, Identifiable>> data;

    public DataManager() {
        this.json = new Json();

        // Initialize schema
        this.dataSchema = new HashMap<>();
        //this.dataSchema.put("class", jsonData -> json.readValue(CharacterClass.class, jsonData));
        //this.dataSchema.put("enemy", jsonData -> json.readValue(Enemy.class, jsonData));
        //this.dataSchema.put("bullet", jsonData -> json.readValue(Bullet.class, jsonData));
        //this.dataSchema.put("item", jsonData -> json.readValue(Item.class, jsonData));

        this.data = new HashMap<>();
        for (String key : dataSchema.keySet()) {
            this.data.put(key, new HashMap<>());
        }
    }

    /*public CharacterClass getCharacterClass(String id) {
        return (CharacterClass) data.get("class").get(id);
    }

    public Enemy getEnemy(String id) {
        return (Enemy) data.get("enemy").get(id);
    }

    public Bullet getBullet(String id) {
        return (Bullet) data.get("bullet").get(id);
    }

    public Item getItem(String id) {
        return (Item) data.get("item").get(id);
    }*/

    public void load() {
        FileHandle baseDir = Gdx.files.internal("data");

        if (!baseDir.exists()) {
            Gdx.app.log("DataManager", "Directory 'data/' does not exist.");
            return;
        }

        for (Map.Entry<String, Parser> entry : dataSchema.entrySet()) {
            String folder = entry.getKey();
            Parser parser = entry.getValue();

            FileHandle folderHandle = baseDir.child(folder);
            if (!folderHandle.exists()) {
                Gdx.app.log("DataManager", "Folder '" + folderHandle.path() + "' does not exist, skipping.");
                continue;
            }

            for (FileHandle file : folderHandle.list("json")) {
                try {
                    Identifiable obj = parser.parse(file);
                    data.get(folder).put(obj.getId(), obj);
                } catch (Exception e) {
                    Gdx.app.error("DataManager", "Error parsing " + file.name() + " in " + folder + ": " + e.getMessage());
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
