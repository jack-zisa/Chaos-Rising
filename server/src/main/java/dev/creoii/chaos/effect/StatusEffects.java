package dev.creoii.chaos.effect;

import java.util.HashMap;
import java.util.Map;

public final class StatusEffects {
    public static final Map<String, ServerStatusEffect> ALL = new HashMap<>();

    static {
        ServerStatusEffect.register("regeneration", (entity, statusEffect) -> {
            entity.heal(statusEffect.getAmplifier());
        });
        ServerStatusEffect.register("poison", (entity, statusEffect) -> {
            entity.damage(statusEffect.getAmplifier());
        });
        ServerStatusEffect.register("invulnerable");
    }
}
