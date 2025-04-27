package dev.creoii.chaos.entity.behavior.bulletpath;

import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public record EmptyBulletPath() implements BulletPath {
    @Override
    public float speed(EntityController<? extends BulletEntity> controller) {
        return 0f;
    }

    @Override
    public void update(EntityController<? extends BulletEntity> controller, int gametime, float dt) {
    }

    @Override
    public BulletPath copy() {
        return this;
    }
}
