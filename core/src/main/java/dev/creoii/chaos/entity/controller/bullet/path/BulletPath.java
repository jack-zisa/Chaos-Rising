package dev.creoii.chaos.entity.controller.bullet.path;

import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.bullet.BulletController;

public interface BulletPath {
    float speed(Game game);

    void update(BulletController controller, int gametime, float dt);

    BulletPath copy();
}
