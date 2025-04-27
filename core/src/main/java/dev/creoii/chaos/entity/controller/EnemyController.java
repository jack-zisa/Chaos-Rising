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

    public Behavior getBehavior() {
        return behavior;
    }

    @Override
    public int getTime() {
        return time;
    }

    public void start(EnemyEntity entity) {
        if (behavior == null)
            return;
        this.entity = entity;
        time = entity.getGame().getGametime();
        behavior.start(this, entity);
    }

    @Override
    public void control(int gametime, float delta) {
        if (entity != null && behavior != null) {
            behavior.update(this, ++time, delta);
        }
    }
}
