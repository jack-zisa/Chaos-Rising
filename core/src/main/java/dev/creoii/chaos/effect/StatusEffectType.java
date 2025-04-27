package dev.creoii.chaos.effect;

import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.util.Identifiable;

import java.util.function.BiConsumer;

public record StatusEffectType(String id, BiConsumer<LivingEntity, StatusEffect> starter, BiConsumer<LivingEntity, StatusEffect> applier, BiConsumer<LivingEntity, StatusEffect> remover) implements Identifiable {
    static void register(String id, BiConsumer<LivingEntity, StatusEffect> starter, BiConsumer<LivingEntity, StatusEffect> applier, BiConsumer<LivingEntity, StatusEffect> remover) {
        StatusEffectTypes.ALL.put(id, new StatusEffectType(id, starter, applier, remover));
    }

    static void register(String id, BiConsumer<LivingEntity, StatusEffect> applier) {
        StatusEffectTypes.ALL.put(id, new StatusEffectType(id, null, applier, null));
    }

    static void register(String id) {
        StatusEffectTypes.ALL.put(id, new StatusEffectType(id, null, null, null));
    }
}
