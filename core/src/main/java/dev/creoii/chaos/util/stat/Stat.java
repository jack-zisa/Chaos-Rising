package dev.creoii.chaos.util.stat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Stat {
    public static final Codec<Stat> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Stat.Type.CODEC.fieldOf("stat_type").forGetter(Stat::type),
            Codec.INT.fieldOf("amount").forGetter(Stat::base),
            ModifierEntry.CODEC.listOf().fieldOf("modifiers").forGetter(Stat::getModifiers)
        ).apply(instance, Stat::new);
    });
    private final Type type;
    private int base;
    private final List<ModifierEntry> modifiers = new ArrayList<>();

    public Stat(Type type, int base) {
        this.type = type;
        this.base = base;
    }

    public Stat(Type type) {
        this(type, 0);
    }

    public Stat(Type type, int base, List<ModifierEntry> modifiers) {
        this.type = type;
        this.base = base;
        modifiers.forEach(this::addModifier);
    }

    public Type type() {
        return type;
    }

    public int base() {
        return base;
    }

    public List<ModifierEntry> getModifiers() {
        return modifiers;
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

    public enum Type {
        HEALTH,
        SPEED,
        ATTACK_SPEED,
        DEFENSE,
        ATTACK,
        VITALITY;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
