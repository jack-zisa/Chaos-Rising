package dev.creoii.chaos.entity.controller.bullet.path;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.controller.bullet.BulletController;
import dev.creoii.chaos.util.provider.Provider;

public record SimpleBulletPath(Provider<Float> speed, Provider<Float> frequency, Provider<Float> amplitude, Provider<Float> arcSpeed) implements BulletPath {
    @Override
    public float speed(Game game) {
        return speed.get(game);
    }

    @Override
    public void update(BulletController controller, int gametime, float dt) {
        Vector2 forward = new Vector2(controller.getEntity().getDirection()).scl(speed.get(controller.getEntity().getGame()) * Entity.COORDINATE_SCALE * dt);
        Vector2 offset = new Vector2(controller.getEntity().getPerpendicular()).scl((float) (Math.cos((gametime - controller.getEntity().getSpawnTime()) * frequency.get(controller.getEntity().getGame())) * amplitude.get(controller.getEntity().getGame())) * controller.getEntity().getIndex());
        controller.getEntity().getPos().add(forward).add(offset);

        float angle = (float) (Math.atan2(controller.getEntity().getDirection().y, controller.getEntity().getDirection().x) + arcSpeed.get(controller.getEntity().getGame()));
        controller.getEntity().getDirection().set((float) Math.cos(angle), (float) Math.sin(angle));
    }

    @Override
    public BulletPath copy() {
        return new SimpleBulletPath(speed.copy(), frequency.copy(), amplitude.copy(), arcSpeed.copy());
    }
}
