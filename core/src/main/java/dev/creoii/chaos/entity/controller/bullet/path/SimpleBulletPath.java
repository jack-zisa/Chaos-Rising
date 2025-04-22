package dev.creoii.chaos.entity.controller.bullet.path;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.controller.bullet.BulletController;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.floatprovider.FloatProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record SimpleBulletPath(FloatProvider speed, VecProvider offset, FloatProvider arcSpeed) implements BulletPath {
    @Override
    public float speed(BulletController controller) {
        return speed.get(Provider.Context.of(controller.getEntity(), controller.getEntity().getGame().getGametime()));
    }

    @Override
    public void update(BulletController controller, int gametime, float dt) {
        Provider.Context context = Provider.Context.of(controller.getEntity(), controller.getEntity().getGame().getGametime());
        float speed = this.speed.get(context);
        if (speed == 0)
            return;

        Vector2 forward = new Vector2(controller.getEntity().getDirection()).scl(speed * Entity.COORDINATE_SCALE * dt);
        controller.getEntity().getPos().add(forward).add(offset.get(context));

        float angle = (float) (Math.atan2(controller.getEntity().getDirection().y, controller.getEntity().getDirection().x) + arcSpeed.init(gametime).get(context));
        controller.getEntity().getDirection().set((float) Math.cos(angle), (float) Math.sin(angle));
    }

    @Override
    public BulletPath copy() {
        return new SimpleBulletPath(speed.copy(), offset.copy(), arcSpeed.copy());
    }
}
