package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.stat.StatContainer;

public abstract class LivingEntity extends Entity {
    public static final StatContainer DEFAULT_STAT_CONTAINER = new StatContainer(10, 1, 1, 0, 1, 1);
    private final StatContainer statContainer;
    private final StatContainer maxStatContainer;

    public LivingEntity(String textureId, float scale, Vector2 collider, Group group, StatContainer statContainer, StatContainer maxStatContainer) {
        super(textureId, scale, collider, group);
        this.statContainer = statContainer;
        this.maxStatContainer = maxStatContainer;
    }

    public StatContainer getStats() {
        return statContainer;
    }

    public StatContainer getMaxStats() {
        return maxStatContainer;
    }

    public void damage(int amount) {
        if (statContainer.health.value() <= 0)
            return;
        amount = Math.clamp(amount - statContainer.defense.value(), 0, amount);
        statContainer.health.set(Math.max(0, statContainer.health.value() - Math.max(0, amount - statContainer.defense.value())));
    }

    public void heal(int amount) {
        if (statContainer.health.value() <= 0)
            return;
        statContainer.health.set(Math.min(maxStatContainer.health.value(), statContainer.health.value() + amount));
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        if (statContainer.health != maxStatContainer.health && gametime % 40 == 0)
            heal(Math.round(1f + .2f * statContainer.vitality.value()));
    }
}
