package dev.creoii.chaos.entity.controller.bullet.path;

import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.bullet.BulletController;

public record EmptyBulletPath() implements BulletPath {
    @Override
    public float speed(Game game) {
        return 0f;
    }

    @Override
    public void update(BulletController controller, int gametime, float dt) {
    }

    @Override
    public BulletPath copy() {
        return this;
    }
}
