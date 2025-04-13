package dev.creoii.chaos.effect;

import dev.creoii.chaos.util.stat.StatContainer;
import dev.creoii.chaos.util.stat.StatModifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class StatusEffects {
    public static final Map<String, StatusEffect> ALL = new HashMap<>();

    static {
        StatusEffect.register("regeneration", (entity, statusEffect) -> {
            UUID uuid = UUID.randomUUID();
            statusEffect.getData().put("uuid", uuid);
            System.out.println("start " + statusEffect.id());
            entity.getStats().applyModifier(new StatModifier(uuid, StatModifier.Type.ADD, new StatContainer().withVitality(10)));
        }, (entity, statusEffect) -> {
            System.out.println("apply " + statusEffect.id() + " " + statusEffect.getDuration());
        }, (entity, statusEffect) -> {
            entity.getStats().removeModifier((UUID) statusEffect.getData().get("uuid"));
            System.out.println("end " + statusEffect.id());
        });
        StatusEffect.register("invulnerable");
    }
}
