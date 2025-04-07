package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.ai.controller.EntityController;
import dev.creoii.chaos.util.stat.Stats;

public class EnemyEntity extends LivingEntity {
    public EnemyEntity(Vector2 collider) {
        super(collider, Group.ENEMY, new Stats(), new Stats());
    }

    @Override
    public void collide(LivingEntity other) {
    }

    @Override
    public EntityController<?> getController() {
        return null;
    }
}
