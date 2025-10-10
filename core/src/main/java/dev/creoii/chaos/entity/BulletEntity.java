package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.BulletController;
import dev.creoii.chaos.entity.serialization.BulletData;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.util.provider.Provider;

import javax.annotation.Nullable;

public class BulletEntity extends Entity {
    private Entity parent;
    private final Vector2 direction;
    private final int lifetime;
    private final int damage;
    private final int index;
    private final BulletController controller;

    public BulletEntity(Game game, EntityType<? extends BulletEntity> type, int id, Vector2 pos, Vector2 direction, int lifetime, int damage, int index) {
        super(game, type, id, pos);
        this.direction = direction;
        this.lifetime = lifetime;
        this.damage = damage;
        this.index = index;

        if (!game.isClient()) {
            controller = new BulletController(this);
        } else controller = null;
    }

    @Nullable
    @Override
    public EntityCustomData getCustomPacketData() {
        return new BulletData(0f, 0f);
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

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        if (gametime - getSpawnTime() >= lifetime) {
            remove();
        }

        controller.control(gametime, delta);
    }

    @Override
    public void collisionEnter(Entity other) {
        if (other instanceof LivingEntity living && parent != null && other.getType().group() != parent.getType().group()) {
            living.damage(damage);
            if (!((BulletEntityType) getType()).piercing().get(Provider.Context.of(this, getGame().getGametime()))) {
                remove();
            }
        }
    }

    @Override
    public boolean canMove() {
        return controller.getPath().speed(controller) > 0f;
    }
}
