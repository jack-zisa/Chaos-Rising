package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.network.s2c.EntityDamageS2C;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.ArrayList;
import java.util.List;

public abstract class LivingEntity extends Entity {
    private final StatContainer statContainer;
    private final StatContainer maxStatContainer;
    private final List<StatusEffect> statusEffects;

    public LivingEntity(Game game, EntityType<? extends LivingEntity> type, int id, Vector2 pos, StatContainer statContainer, StatContainer maxStatContainer) {
        super(game, type, id, pos);
        this.statContainer = statContainer;
        this.maxStatContainer = statContainer;
        statusEffects = new ArrayList<>();
    }

    public StatContainer getStats() {
        return statContainer;
    }

    public StatContainer getMaxStats() {
        return maxStatContainer;
    }

    @Override
    public boolean canMove() {
        return statContainer.speed().value() > 0;
    }

    public void damage(int amount) {
        if (statContainer.health().value() <= 0 || hasStatusEffect("invulnerable"))
            return;
        amount = Math.max(0, amount - statContainer.defense().value());
        statContainer.health().set(Math.max(0, statContainer.health().value() - amount));

        if (!getGame().isClient()) {
            getGame().getServer().sendToAllTCP(new EntityDamageS2C(getId(), amount));
        }

        if (statContainer.health().value() <= 0) {
            remove();
        }
    }

    public void heal(int amount) {
        if (statContainer.health().value() <= 0)
            return;
        statContainer.health().set(Math.min(maxStatContainer.health().value(), statContainer.health().value() + amount));
    }

    public List<StatusEffect> getStatusEffects() {
        return statusEffects;
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
        return statusEffects.stream().anyMatch(statusEffect1 -> statusEffect1.getType().id().equals(id));
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        for (int i = getStatusEffects().size() - 1; i >= 0; --i) {
            StatusEffect statusEffect = getStatusEffects().get(i);

            if (statusEffect.getType().applier() != null)
                statusEffect.getType().applier().accept(this, statusEffect);

            if (statusEffect.getDuration() > 0) {
                statusEffect.decrementDuration();
            } else {
                removeStatusEffect(statusEffect);
            }
        }

        if (statContainer.health().value() <= maxStatContainer.health().value() && gametime % 40 == 0)
            heal(Math.round(1f + .2f * statContainer.vitality().value()));
    }
}
