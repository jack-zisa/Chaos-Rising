package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.World;
import dev.creoii.chaos.entity.controller.BulletController;
import dev.creoii.chaos.entity.serialization.BulletData;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.util.provider.Provider;

import javax.annotation.Nullable;
import java.util.Map;

public class BulletEntity extends Entity {
    private Entity parent;
    private Vector2 direction;
    private int lifetime;
    private int damage;
    private int index;
    private final BulletController controller;

    public BulletEntity(World world, EntityType<? extends BulletEntity> type, int id, Vector2 pos, Vector2 direction, int lifetime, int damage, int index) {
        super(world, type, id, pos);
        this.direction = direction;
        this.lifetime = lifetime;
        this.damage = damage;
        this.index = index;

        if (!world.getGame().isClient()) {
            controller = new BulletController(this);
        } else controller = null;
    }

    @Override
    public void reinit(int id, Vector2 pos, Map<String, Object> data) {
        super.reinit(id, pos, data);
        direction = (Vector2) data.get("direction");
        lifetime = (int) data.getOrDefault("lifetime", 0);
        damage = (int) data.getOrDefault("damage", 0);
        index = (int) data.getOrDefault("index", 0);
    }

    @Nullable
    @Override
    public EntityCustomData getCustomPacketData() {
        return new BulletData(getType().id(), 0f, 0f);
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

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void tick(int gametime, float delta) {
        if (gametime - getSpawnTime() >= lifetime) {
            remove();
        }

        controller.control(gametime, delta);
    }

    @Override
    public void collisionEnter(Entity other) {
        if (other instanceof LivingEntity living && parent != null && other.getType().group() != parent.getType().group()) {
            living.damage(damage);
            if (!((BulletEntityType) getType()).piercing().get(Provider.Context.of(this, getWorld().getGame().getGametime()))) {
                remove();
            }
        }
    }

    @Override
    public boolean canMove() {
        return controller.getPath().speed(controller) > 0f;
    }
}
