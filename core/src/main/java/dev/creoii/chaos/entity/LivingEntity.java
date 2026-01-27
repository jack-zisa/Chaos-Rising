package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.World;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.network.s2c.EntityDamageS2C;
import dev.creoii.chaos.network.s2c.LivingStatUpdateS2C;
import dev.creoii.chaos.network.s2c.StatusEffectS2C;
import dev.creoii.chaos.util.event.DamageEntityEvent;
import dev.creoii.chaos.util.stat.StatContainer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.ArrayList;
import java.util.List;

public abstract class LivingEntity extends Entity {
    private final StatContainer statContainer;
    private final StatContainer maxStatContainer;
    private final List<StatusEffect.Instance> statusEffects;
    private final IntList children;
    private int parentId;

    public LivingEntity(World world, EntityType<? extends LivingEntity> type, int id, Vector2 pos, StatContainer statContainer, StatContainer maxStatContainer) {
        super(world, type, id, pos);
        this.statContainer = statContainer;
        this.maxStatContainer = maxStatContainer;
        statusEffects = new ArrayList<>();
        children = new IntArrayList();
        parentId = -1;
    }

    public StatContainer getStats() {
        return statContainer;
    }

    public StatContainer getMaxStats() {
        return maxStatContainer;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int id) {
        parentId = id;
    }

    public IntList getChildren() {
        return children;
    }

    public boolean hasParent() {
        return parentId == -1;
    }

    public void addChild(int id) {
        children.add(id);
    }

    public void removeChild(int id) {
        children.removeInt(id);
    }

    @Override
    public boolean canMove() {
        return statContainer.speed().value() > 0;
    }

    public void damage(int amount) {
        if (statContainer.health().value() <= 0 || hasStatusEffect(StatusEffect.Type.INVULNERABLE))
            return;

        amount = Math.max(0, amount - statContainer.defense().value());
        statContainer.health().set(Math.max(0, statContainer.health().value() - amount));

        DamageEntityEvent.EVENT.invoker().onDamageEntity(getWorld(), amount, getId(), -1);

        if (statContainer.health().value() <= 0) {
            remove();
        } else if (!getWorld().getGame().isClient()) {
            getWorld().getGame().getServer().sendToAllTCP(new EntityDamageS2C(getId(), amount));
        }
    }

    public void heal(int amount) {
        if (statContainer.health().value() <= 0)
            return;
        statContainer.health().set(Math.min(maxStatContainer.health().value(), statContainer.health().value() + amount));

        if (!getWorld().getGame().isClient()) {
            getWorld().getGame().getServer().sendToAllTCP(new LivingStatUpdateS2C(getId(), statContainer.health(), false));
        }
    }

    public List<StatusEffect.Instance> getStatusEffects() {
        return statusEffects;
    }

    public void addStatusEffect(StatusEffect.Instance instance) {
        statusEffects.add(instance);
        getWorld().getGame().getServer().sendToAllTCP(new StatusEffectS2C(getId(), instance, true));
    }

    public void removeStatusEffect(StatusEffect.Instance instance) {
        statusEffects.remove(instance);
        getWorld().getGame().getServer().sendToAllTCP(new StatusEffectS2C(getId(), instance, false));
    }

    public void clearStatusEffects() {
        statusEffects.forEach(instance -> {
            getWorld().getGame().getServer().sendToAllTCP(new StatusEffectS2C(getId(), instance, false));
        });
        statusEffects.clear();
    }

    public boolean hasStatusEffect(StatusEffect.Type type) {
        return statusEffects.stream().anyMatch(statusEffect1 -> statusEffect1.getEffect().type().equals(type));
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        for (int i = getStatusEffects().size() - 1; i >= 0; --i) {
            StatusEffect.Instance instance = getStatusEffects().get(i);

            if (instance.getEffect().applier() != null)
                instance.getEffect().applier().accept(this, instance);

            if (instance.isInfinite())
                continue;

            if (instance.getDuration() > 0) {
                instance.decrementDuration();
            } else {
                removeStatusEffect(instance);
            }
        }

        int vitality = statContainer.vitality().value();
        if (vitality > 0 && statContainer.health().value() < maxStatContainer.health().value() && gametime % 40 == 0) {
            heal(Math.round(1f + .2f * vitality));
        }
    }
}
