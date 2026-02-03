package dev.creoii.chaos.entity.behavior.action;

import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public abstract class InstantAction extends Action {
    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
    }

    @Override
    public void end(EntityController<? extends EnemyEntity> controller) {
    }

    @Override
    public boolean isInstant() {
        return true;
    }
}
