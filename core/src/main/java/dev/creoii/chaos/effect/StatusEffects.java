package dev.creoii.chaos.effect;

import dev.creoii.chaos.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class StatusEffects {
    public static final Map<StatusEffect.Type, StatusEffect> ALL = new HashMap<>();

    public static final StatusEffect REGENERATION = register(StatusEffect.Type.REGENERATION, (entity, statusEffect) -> {
        entity.heal(statusEffect.getAmplifier());
    });
    public static final StatusEffect POISON = register(StatusEffect.Type.POISON, (entity, statusEffect) -> {
        entity.damage(statusEffect.getAmplifier());
    });
    public static final StatusEffect INVULNERABLE = register(StatusEffect.Type.INVULNERABLE);

    static StatusEffect register(StatusEffect.Type type, BiConsumer<LivingEntity, StatusEffect.Instance> starter, BiConsumer<LivingEntity, StatusEffect.Instance> applier, BiConsumer<LivingEntity, StatusEffect.Instance> remover) {
        return ALL.put(type, new StatusEffect(type, starter, applier, remover));
    }

    static StatusEffect register(StatusEffect.Type type, BiConsumer<LivingEntity, StatusEffect.Instance> applier) {
        return ALL.put(type, new StatusEffect(type, null, applier, null));
    }

    static StatusEffect register(StatusEffect.Type type) {
        return ALL.put(type, new StatusEffect(type, null, null, null));
    }
}
