package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.util.stat.Stats;

public record CharacterClass(String id, String spritePath, Stats baseStats, Stats maxStats) implements DataManager.Identifiable {
    @Override
    public String getId() {
        return id;
    }

    public static class Serializer implements Json.Serializer<CharacterClass> {
        @Override
        public void write(Json json, CharacterClass characterClass, Class knownType) {
            json.writeObjectStart();
            json.writeValue("id", characterClass.id);
            json.writeValue("sprite_path", characterClass.spritePath.substring(6));
            json.writeValue("base_stats", characterClass.baseStats);
            json.writeValue("max_stats", characterClass.maxStats);
            json.writeObjectEnd();
        }

        @Override
        public CharacterClass read(Json json, JsonValue jsonValue, Class aClass) {
            String id = jsonValue.getString("id");
            String spritePath = jsonValue.getString("sprite_path", id);
            Stats baseStats = json.readValue(Stats.class, jsonValue.get("base_stats"));
            Stats maxStats = json.readValue(Stats.class, jsonValue.get("max_stats"));
            return new CharacterClass(id, spritePath, baseStats, maxStats);
        }
    }
}
