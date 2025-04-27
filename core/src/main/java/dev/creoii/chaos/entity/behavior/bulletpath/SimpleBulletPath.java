package dev.creoii.chaos.entity.behavior.bulletpath;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.ServerEntity;
import dev.creoii.chaos.entity.controller.bullet.BulletController;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record SimpleBulletPath(NumberProvider speed, VecProvider offset, NumberProvider arcSpeed) implements BulletPath {
    @Override
    public float speed(BulletController controller) {
        return speed.init(controller.getEntity().getGame().getGametime()).get(Provider.Context.of(controller.getEntity(), controller.getEntity().getGame().getGametime()));
    }

    @Override
    public void update(BulletController controller, int gametime, float dt) {
        Provider.Context context = Provider.Context.of(controller.getEntity(), controller.getEntity().getGame().getGametime());
        float speed = this.speed.init(gametime).get(context);
        if (speed == 0)
            return;

        Vector2 forward = new Vector2(controller.getEntity().getDirection()).scl(speed * ServerEntity.COORDINATE_SCALE * dt);
        controller.getEntity().getPos().add(forward).add(offset.get(context));

        float angle = (float) (Math.atan2(controller.getEntity().getDirection().y, controller.getEntity().getDirection().x) + arcSpeed.init(gametime).get(context));
        controller.getEntity().getDirection().set((float) Math.cos(angle), (float) Math.sin(angle));
    }

    @Override
    public BulletPath copy() {
        return new SimpleBulletPath(speed.copy(), offset.copy(), arcSpeed.copy());
    }
}
