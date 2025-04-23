package dev.creoii.chaos.util.stat;

import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Stat {
    private final Type type;
    private int base;
    private final List<ModifierEntry> modifiers = new ArrayList<>();

    public Stat(Type type, int base) {
        this.type = type;
        this.base = base;
    }

    public Type type() {
        return type;
    }

    public int base() {
        return base;
    }

    public void set(int value) {
        this.base = value;
    }

    public void addModifier(ModifierEntry modifierEntry) {
        modifiers.add(modifierEntry);
    }

    public void removeModifier(UUID uuid) {
        modifiers.removeIf(modifier -> modifier.uuid().equals(uuid));
    }

    public int value() {
        int result = base;
        for (ModifierEntry mod : modifiers) {
            switch (mod.operation()) {
                case ADD -> result += mod.amount();
                case SET -> result = mod.amount();
                case MULTIPLY -> result *= mod.amount();
            }
        }
        return Math.max(0, result);
    }

    @Override
    public String toString() {
        return String.valueOf(value());
    }

    public static Stat parse(JsonValue jsonValue, Type type) {
        if (jsonValue != null) {
            if (jsonValue.isNumber()) {
                return new Stat(type, jsonValue.asInt());
            } else if (jsonValue.isObject()) {
                Stat stat = new Stat(type, jsonValue.getInt("value"));
                jsonValue.get("entries").forEach(entryValue -> stat.addModifier(ModifierEntry.parse(entryValue)));
                return stat;
            }
        }
        return new Stat(type, 0);
    }

    public enum Type {
        HEALTH,
        SPEED,
        ATTACK_SPEED,
        DEFENSE,
        ATTACK,
        VITALITY
    }
}
