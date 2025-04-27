package dev.creoii.chaos.entity.controller.bullet;

import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.BulletEntityType;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.behavior.bulletpath.BulletPath;

public class BulletController extends EntityController<BulletEntity> {
    private final BulletPath path;

    public BulletController(BulletEntity bullet) {
        super(bullet);
        path = ((BulletEntityType) bullet.getType()).path().copy();
    }

    @Override
    public int getTime() {
        return 0;
    }

    @Override
    public void control(int gametime, float dt) {
        path.update(this, gametime, dt);
    }
}
