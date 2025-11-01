package dev.creoii.chaos.entity.controller;

import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.entity.behavior.MultiBehavior;
import dev.creoii.chaos.entity.behavior.SimpleBehavior;
import dev.creoii.chaos.entity.behavior.action.Action;
import dev.creoii.chaos.entity.behavior.phase.Phase;

import java.util.List;

public class EnemyController extends EntityController<EnemyEntity> {
    private final Behavior behavior;
    private int time;

    public EnemyController(Behavior behavior) {
        super(null);
        this.behavior = behavior;

        if (behavior instanceof SimpleBehavior(List<Action> actions)) {
            actions.forEach(action -> action.end(this));
        } else if (behavior instanceof MultiBehavior multiBehavior) {
            for (Phase phase : multiBehavior.getPhases()) {
                phase.end(this);
            }
        }
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
