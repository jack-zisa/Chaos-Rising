package dev.creoii.chaos.util.stat;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.UUID;

public record StatModifier(UUID uuid, Type type, StatContainer statContainer) {
    public static final StatModifier NONE = new StatModifier(UUID.nameUUIDFromBytes("none".getBytes()), Type.NONE, new StatContainer());

    public static StatModifier parse(Json json, JsonValue jsonValue) {
        Type type = jsonValue.has("type") ? Type.valueOf(jsonValue.getString("type").toUpperCase()) : Type.ADD;
        StatContainer statContainer = json.readValue(StatContainer.class, jsonValue.get("stats"));
        return new StatModifier(UUID.randomUUID(), type, statContainer);
    }

    public enum Type {
        NONE,
        ADD,
        MULTIPLY,
        SET
    }
}
