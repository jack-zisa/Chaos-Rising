package dev.creoii.chaos.effect;

import java.util.HashMap;
import java.util.Map;

public final class StatusEffects {
    public static final Map<String, StatusEffect> ALL = new HashMap<>();

    static {
        StatusEffect.register("regeneration", (entity, statusEffect) -> {
            entity.heal(statusEffect.getAmplifier());
        });
        StatusEffect.register("poison", (entity, statusEffect) -> {
            entity.damage(statusEffect.getAmplifier());
        });
        StatusEffect.register("invulnerable");
    }
}
