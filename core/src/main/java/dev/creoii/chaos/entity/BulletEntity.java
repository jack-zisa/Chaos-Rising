package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;

import java.util.UUID;

public class BulletEntity extends Entity {
    private Entity parent;
    private final Vector2 direction;
    private final int lifetime;
    private final int damage;
    private final int index;

    public BulletEntity(Game game, EntityType<? extends BulletEntity> type, UUID uuid, Vector2 pos, Vector2 direction, int lifetime, int damage, int index) {
        super(game, type, uuid, pos);
        this.direction = direction;
        this.lifetime = lifetime;
        this.damage = damage;
        this.index = index;
        //float angle = (float) Math.atan2(yDir, xDir) * (180f / (float) Math.PI) % 360f;
        //getSprite().setOriginCenter();
        //getSprite().setRotation(angle - angleOffset.get(Provider.Context.of(this, game.getGametime())));
    }

    public Entity getParent() {
        return parent;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public int getLifetime() {
        return lifetime;
    }

    public int getDamage() {
        return damage;
    }

    public int getIndex() {
        return index;
    }
}
