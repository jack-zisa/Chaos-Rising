package dev.creoii.chaos.effect;

import java.util.HashMap;
import java.util.Map;

public final class StatusEffectTypes {
    public static final Map<String, StatusEffectType> ALL = new HashMap<>();

    static {
        StatusEffectType.register("regeneration", (entity, statusEffect) -> {
            entity.heal(statusEffect.getAmplifier());
        });
        StatusEffectType.register("poison", (entity, statusEffect) -> {
            entity.damage(statusEffect.getAmplifier());
        });
        StatusEffectType.register("invulnerable");
    }
}
