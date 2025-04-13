package dev.creoii.chaos.entity.controller;

import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.behavior.Behavior;

public class EnemyController extends EntityController<EnemyEntity> {
    private final Behavior behavior;
    private int time;

    public EnemyController(Behavior behavior) {
        super(null);
        this.behavior = behavior;
    }

    public EnemyController(EnemyController controller) {
        super(null);
        this.behavior = Behavior.copy(controller.behavior);
    }

    public EnemyEntity getEntity() {
        return entity;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public int getTime() {
        return time;
    }

    public void start(EnemyEntity entity) {
        this.entity = entity;
        time = entity.getGame().getGametime();
        behavior.start(this, entity);
    }

    @Override
    public void control(int gametime, float delta) {
        if (entity != null) {
            behavior.update(this, ++time, delta);
        }
    }
}
