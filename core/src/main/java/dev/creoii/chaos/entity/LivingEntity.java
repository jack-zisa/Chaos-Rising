package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.ArrayList;
import java.util.List;

public abstract class LivingEntity extends Entity {
    public static final StatContainer DEFAULT_STAT_CONTAINER = new StatContainer(10, 1, 1, 0, 1, 1);
    private final StatContainer statContainer;
    private final StatContainer maxStatContainer;
    private final List<StatusEffect> statusEffects;

    public LivingEntity(String textureId, float scale, Vector2 collider, Group group, StatContainer statContainer, StatContainer maxStatContainer) {
        super(textureId, scale, collider, group);
        this.statContainer = statContainer;
        this.maxStatContainer = maxStatContainer;
        statusEffects = new ArrayList<>();
    }

    public StatContainer getStats() {
        return statContainer;
    }

    public StatContainer getMaxStats() {
        return maxStatContainer;
    }

    public List<StatusEffect> getStatusEffects() {
        return statusEffects;
    }

    public void damage(int amount) {
        if (statContainer.health.value() <= 0 || hasStatusEffect("invulnerable"))
            return;
        amount = Math.clamp(amount - statContainer.defense.value(), 0, amount);
        statContainer.health.set(Math.max(0, statContainer.health.value() - Math.max(0, amount - statContainer.defense.value())));
    }

    public void heal(int amount) {
        if (statContainer.health.value() <= 0)
            return;
        statContainer.health.set(Math.min(maxStatContainer.health.value(), statContainer.health.value() + amount));
    }

    public void addStatusEffect(StatusEffect statusEffect, int amplifier, int duration) {
        statusEffect.init(amplifier, duration);
        if (statusEffect.getStarter() != null)
            statusEffect.getStarter().accept(this, statusEffect);
        statusEffects.add(statusEffect);
    }

    public void removeStatusEffect(StatusEffect statusEffect) {
        statusEffects.remove(statusEffect);
        if (statusEffect.getRemover() != null)
            statusEffect.getRemover().accept(this, statusEffect);
    }

    public void clearStatusEffects() {
        statusEffects.clear();
    }

    public boolean hasStatusEffect(String id) {
        return statusEffects.stream().anyMatch(statusEffect1 -> statusEffect1.id().equals(id));
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        for (int i = getStatusEffects().size() - 1; i >= 0; --i) {
            StatusEffect statusEffect = getStatusEffects().get(i);

            if (statusEffect.getApplier() != null)
                statusEffect.getApplier().accept(this, statusEffect);

            if (statusEffect.getDuration() > 0) {
                statusEffect.decrementDuration();
            } else {
                removeStatusEffect(statusEffect);
            }
        }

        if (statContainer.health != maxStatContainer.health && gametime % 40 == 0)
            heal(Math.round(1f + .2f * statContainer.vitality.value()));
    }
}
