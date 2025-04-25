package dev.creoii.chaos.entity.character;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.stat.StatContainer;

public record CharacterClass(String id, String textureId, float scale, StatContainer baseStatContainer, StatContainer maxStatContainer) implements DataManager.Identifiable {
    @Override
    public float scale() {
        return scale * Entity.COORDINATE_SCALE;
    }

    public static CharacterClass parse(String id, JsonValue jsonValue) {
        String textureId = jsonValue.getString("texture");
        float scale = jsonValue.getFloat("scale", 1f);
        StatContainer baseStatContainer = StatContainer.parse(jsonValue.get("base_stats"));
        StatContainer maxStatContainer = StatContainer.parse(jsonValue.get("max_stats"));
        return new CharacterClass(id, textureId, scale, baseStatContainer, maxStatContainer);
    }
}
