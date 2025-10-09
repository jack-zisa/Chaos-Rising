package dev.creoii.chaos.util.stat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record ModifierEntry(Stat.Type type, UUID uuid, int amount, Operation operation, ModifierType modifierType) implements Serializable {
    public static final Codec<ModifierEntry> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Stat.Type.CODEC.fieldOf("stat_type").forGetter(ModifierEntry::type),
            Codecs.UUID.fieldOf("uuid").forGetter(ModifierEntry::uuid),
            Codec.INT.fieldOf("amount").orElse(0).forGetter(ModifierEntry::amount),
            Operation.CODEC.fieldOf("operation").orElse(Operation.NONE).forGetter(ModifierEntry::operation),
            ModifierType.CODEC.fieldOf("modifier_type").orElse(ModifierType.ALL).forGetter(ModifierEntry::modifierType)
        ).apply(instance, ModifierEntry::new);
    });

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
        ALL;

        public static final Codec<ModifierType> CODEC = Codec.STRING.xmap(s -> ModifierType.valueOf(s.toUpperCase()), modifierType -> modifierType.name().toLowerCase());
    }

    public enum Operation {
        NONE(""),
        ADD("+"),
        MULTIPLY("x"),
        SET("=");

        public static final Codec<Operation> CODEC = Codec.STRING.xmap(s -> Operation.valueOf(s.toUpperCase()), operation -> operation.name().toLowerCase());
        private final String prefix;

        Operation(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
