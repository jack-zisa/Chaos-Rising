package dev.creoii.chaos.entity.controller;

import dev.creoii.chaos.entity.ServerEnemyEntity;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.entity.behavior.EntityController;

public class EnemyController extends EntityController<ServerEnemyEntity> {
    private final Behavior behavior;
    private int time;

    public EnemyController(Behavior behavior) {
        super(null);
        this.behavior = behavior;
    }

    public ServerEnemyEntity getEntity() {
        return entity;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public int getTime() {
        return time;
    }

    public void start(ServerEnemyEntity entity) {
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
