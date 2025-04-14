package dev.creoii.chaos.entity.controller.bullet;

import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public class BulletController extends EntityController<BulletEntity> {
    public BulletController(BulletEntity bullet) {
        super(bullet);
    }

    @Override
    public void control(int gametime, float dt) {
        entity.getPath().update(this, gametime, dt);
    }
}
