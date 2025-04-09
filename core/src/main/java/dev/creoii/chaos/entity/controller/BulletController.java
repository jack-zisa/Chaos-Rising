package dev.creoii.chaos.entity.controller;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;

public class BulletController extends EntityController<BulletEntity> {
    public BulletController(BulletEntity bullet) {
        super(bullet);
    }

    @Override
    public void control(int gametime, float dt) {
        if (entity instanceof BulletEntity bullet) {
            Vector2 forward = new Vector2(bullet.getDirection()).scl(bullet.getSpeed() * Entity.COORDINATE_SCALE * dt);
            Vector2 offset = new Vector2(bullet.getPerpendicular()).scl((float) (Math.cos((gametime - bullet.getSpawnTime()) * bullet.getFrequency()) * bullet.getAmplitude()) * bullet.getIndex());
            bullet.getPos().add(forward).add(offset);

            float angle = (float) (Math.atan2(bullet.getDirection().y, bullet.getDirection().x) + bullet.getArcSpeed());
            bullet.getDirection().set((float) Math.cos(angle), (float) Math.sin(angle));
        }
    }
}
