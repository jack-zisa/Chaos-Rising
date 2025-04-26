package dev.creoii.chaos.entity;

import dev.creoii.chaos.effect.ServerStatusEffect;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.ArrayList;
import java.util.List;

public abstract class ServerLivingEntity extends ServerEntity {
    private final StatContainer statContainer;
    private final StatContainer maxStatContainer;
    private final List<ServerStatusEffect> statusEffects;

    public ServerLivingEntity(EntityType<? extends ServerLivingEntity> type, EntityGroup group, StatContainer statContainer, StatContainer maxStatContainer) {
        super(type, group);
        this.statContainer = statContainer;
        this.maxStatContainer = maxStatContainer;
        statusEffects = new ArrayList<>();
    }

    public abstract void onDeath();

    public StatContainer getStats() {
        return statContainer;
    }

    public StatContainer getMaxStats() {
        return maxStatContainer;
    }

    public List<ServerStatusEffect> getStatusEffects() {
        return statusEffects;
    }

    public void damage(int amount) {
        if (statContainer.health.value() <= 0 || hasStatusEffect("invulnerable"))
            return;
        amount = Math.max(0, amount - statContainer.defense.value());
        statContainer.health.set(Math.max(0, statContainer.health.value() - amount));

        if (statContainer.health.value() <= 0) {
            remove();
        }
    }

    @Override
    public void remove() {
        onDeath();
        super.remove();
    }

    public void heal(int amount) {
        if (statContainer.health.value() <= 0)
            return;
        statContainer.health.set(Math.min(maxStatContainer.health.value(), statContainer.health.value() + amount));
    }

    public void addStatusEffect(ServerStatusEffect statusEffect, int amplifier, int duration) {
        statusEffect.init(amplifier, duration);
        if (statusEffect.getStarter() != null)
            statusEffect.getStarter().accept(this, statusEffect);
        statusEffects.add(statusEffect);
    }

    public void removeStatusEffect(ServerStatusEffect statusEffect) {
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
            ServerStatusEffect statusEffect = getStatusEffects().get(i);

            if (statusEffect.getApplier() != null)
                statusEffect.getApplier().accept(this, statusEffect);

            if (statusEffect.getDuration() > 0) {
                statusEffect.decrementDuration();
            } else {
                removeStatusEffect(statusEffect);
            }
        }

        if (statContainer.health.value() <= maxStatContainer.health.value() && gametime % 40 == 0)
            heal(Math.round(1f + .2f * statContainer.vitality.value()));
    }
}
