package dev.creoii.chaos.util.stat;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record StatModifier(UUID uuid, Type type, StatContainer statContainer) {
    public static final StatModifier NONE = new StatModifier(UUID.nameUUIDFromBytes("none".getBytes()), Type.NONE, new StatContainer());

    public static StatModifier parse(Json json, JsonValue jsonValue) {
        Type type = jsonValue.has("type") ? Type.valueOf(jsonValue.getString("type").toUpperCase()) : Type.ADD;
        StatContainer statContainer = json.readValue(StatContainer.class, jsonValue.get("stats"));
        return new StatModifier(UUID.randomUUID(), type, statContainer);
    }

    public String getTooltip() {
        String prefix = type.prefix;
        List<String> lines = new ArrayList<>();
        if (statContainer.health.value() > 0)
            lines.add(prefix + statContainer.health.value() + " Health");
        if (statContainer.speed.value() > 0)
            lines.add(prefix + statContainer.speed.value() + " Speed");
        if (statContainer.attackSpeed.value() > 0)
            lines.add(prefix + statContainer.attackSpeed.value() + " Attack Speed");
        if (statContainer.defense.value() > 0)
            lines.add(prefix + statContainer.defense.value() + " Defense");
        if (statContainer.attack.value() > 0)
            lines.add(prefix + statContainer.attack.value() + " Attack");
        if (statContainer.vitality.value() > 0)
            lines.add(prefix + statContainer.vitality.value() + " Vitality");
        StringBuilder builder = new StringBuilder();
        lines.forEach(s -> builder.append(s).append("\n"));
        return builder.toString();
    }

    public enum Type {
        NONE(""),
        ADD("+"),
        MULTIPLY("x"),
        SET("=");

        private final String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
