package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.util.stat.Stats;

public abstract class LivingEntity extends Entity {
    private final Stats stats;
    private final Stats maxStats;

    public LivingEntity(Vector2 collider, Group group, Stats stats, Stats maxStats) {
        super(collider, group);
        this.stats = stats;
        this.maxStats = maxStats;
    }

    public abstract void collide(LivingEntity other);

    public Stats getStats() {
        return stats;
    }

    public Stats getMaxStats() {
        return maxStats;
    }
}
