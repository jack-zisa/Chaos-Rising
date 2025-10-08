package dev.creoii.chaos.util.stat;

import com.badlogic.gdx.utils.JsonValue;

import java.io.Serializable;
import java.util.UUID;

public record ModifierEntry(Stat.Type type, UUID uuid, int amount, Operation operation, ModifierType modifierType) implements Serializable {
    public static ModifierEntry parse(JsonValue jsonValue) {
        Stat.Type type = Stat.Type.valueOf(jsonValue.getString("type").toUpperCase());
        int amount = jsonValue.getInt("amount");
        Operation operation = jsonValue.has("operation") ? Operation.valueOf(jsonValue.getString("operation").toUpperCase()) : Operation.ADD;
        ModifierType modifierType = jsonValue.has("modifier") ? ModifierType.valueOf(jsonValue.getString("modifier").toUpperCase()) : ModifierType.ALL;
        return new ModifierEntry(type, UUID.randomUUID(), amount, operation, modifierType);
    }

    public void apply(StatContainer statContainer) {
        switch (type) {
            case HEALTH -> statContainer.health().addModifier(this);
            case SPEED -> statContainer.speed().addModifier(this);
            case ATTACK_SPEED -> statContainer.attackSpeed().addModifier(this);
            case DEFENSE -> statContainer.defense().addModifier(this);
            case ATTACK -> statContainer.attack().addModifier(this);
            case VITALITY -> statContainer.vitality().addModifier(this);
        }
    }

    public void remove(StatContainer statContainer) {
        switch (type) {
            case HEALTH -> statContainer.health().removeModifier(uuid);
            case SPEED -> statContainer.speed().removeModifier(uuid);
            case ATTACK_SPEED -> statContainer.attackSpeed().removeModifier(uuid);
            case DEFENSE -> statContainer.defense().removeModifier(uuid);
            case ATTACK -> statContainer.attack().removeModifier(uuid);
            case VITALITY -> statContainer.vitality().removeModifier(uuid);
        }
    }

    public enum ModifierType {
        BASE,
        MAX,
        ALL
    }

    public enum Operation {
        NONE(""),
        ADD("+"),
        MULTIPLY("x"),
        SET("=");

        private final String prefix;

        Operation(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
