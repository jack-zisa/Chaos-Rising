package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.util.stat.StatContainer;

public class CharacterClass implements DataManager.Identifiable {
    private String id;
    private final String spritePath;
    private final StatContainer baseStatContainer;
    private final StatContainer maxStatContainer;

    public CharacterClass(String textureId, StatContainer baseStatContainer, StatContainer maxStatContainer) {
        this.spritePath = textureId;
        this.baseStatContainer = baseStatContainer;
        this.maxStatContainer = maxStatContainer;
    }

    @Override
    public String id() {
        return id;
    }

    public String getTextureId() {
        return spritePath;
    }

    public StatContainer getBaseStats() {
        return baseStatContainer;
    }

    public StatContainer getMaxStats() {
        return maxStatContainer;
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
            json.writeValue("base_stats", characterClass.baseStatContainer);
            json.writeValue("max_stats", characterClass.maxStatContainer);
            json.writeObjectEnd();
        }

        @Override
        public CharacterClass read(Json json, JsonValue jsonValue, Class aClass) {
            String spritePath = jsonValue.getString("sprite_path");
            StatContainer baseStatContainer = json.readValue(StatContainer.class, jsonValue.get("base_stats"));
            StatContainer maxStatContainer = json.readValue(StatContainer.class, jsonValue.get("max_stats"));
            return new CharacterClass(spritePath, baseStatContainer, maxStatContainer);
        }
    }
}
