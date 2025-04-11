package dev.creoii.chaos.util.stat;

import java.util.ArrayList;
import java.util.List;

public class Stat {
    private int base;
    private final List<ModifierEntry> modifiers = new ArrayList<>();

    public Stat(int base) {
        this.base = base;
    }

    public int base() {
        return base;
    }

    public void set(int value) {
        this.base = value;
    }

    public void addModifier(int amount, StatModifier.Type type) {
        modifiers.add(new ModifierEntry(amount, type));
    }

    public void removeModifier(int amount, StatModifier.Type type) {
        modifiers.removeIf(m -> m.amount == amount && m.type == type);
    }

    public int value() {
        int result = base;
        for (ModifierEntry mod : modifiers) {
            switch (mod.type) {
                case ADD -> result += mod.amount;
                case SET -> result = mod.amount;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(value());
    }

    private record ModifierEntry(int amount, StatModifier.Type type) {}
}
