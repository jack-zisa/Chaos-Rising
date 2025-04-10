package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.util.stat.Stats;

public class CharacterClass implements DataManager.Identifiable {
    private String id;
    private final String spritePath;
    private final Stats baseStats;
    private final Stats maxStats;

    public CharacterClass(String spritePath, Stats baseStats, Stats maxStats) {
        this.spritePath = spritePath;
        this.baseStats = baseStats;
        this.maxStats = maxStats;
    }

    @Override
    public String id() {
        return id;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public Stats getBaseStats() {
        return baseStats;
    }

    public Stats getMaxStats() {
        return maxStats;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public static class Serializer implements Json.Serializer<CharacterClass> {
        @Override
        public void write(Json json, CharacterClass characterClass, Class knownType) {
            json.writeObjectStart();
            json.writeValue("sprite_path", characterClass.spritePath.substring(6));
            json.writeValue("base_stats", characterClass.baseStats);
            json.writeValue("max_stats", characterClass.maxStats);
            json.writeObjectEnd();
        }

        @Override
        public CharacterClass read(Json json, JsonValue jsonValue, Class aClass) {
            String spritePath = jsonValue.getString("sprite_path");
            Stats baseStats = json.readValue(Stats.class, jsonValue.get("base_stats"));
            Stats maxStats = json.readValue(Stats.class, jsonValue.get("max_stats"));
            return new CharacterClass(spritePath, baseStats, maxStats);
        }
    }
}
