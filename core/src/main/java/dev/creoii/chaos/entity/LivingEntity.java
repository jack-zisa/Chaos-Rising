package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LivingEntity extends Entity {
    private final StatContainer statContainer;
    private final StatContainer maxStatContainer;
    private final List<StatusEffect> statusEffects;

    public LivingEntity(Game game, UUID uuid, Vector2 pos, float scale) {
        super(game, uuid, pos, scale);
        statContainer = new StatContainer();
        maxStatContainer = new StatContainer();
        statusEffects = new ArrayList<>();
    }

    public StatContainer getStats() {
        return statContainer;
    }

    public StatContainer getMaxStats() {
        return maxStatContainer;
    }

    public void addStatusEffect(StatusEffect statusEffect) {
        statusEffects.add(statusEffect);
    }

    public void removeStatusEffect(StatusEffect statusEffect) {
        statusEffects.remove(statusEffect);
    }

    public void clearStatusEffects() {
        statusEffects.clear();
    }

    public boolean hasStatusEffect(String id) {
        return statusEffects.stream().anyMatch(statusEffect1 -> statusEffect1.id().equals(id));
    }
}
