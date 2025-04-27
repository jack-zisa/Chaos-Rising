package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.controller.bullet.BulletController;
import dev.creoii.chaos.entity.behavior.EntityController;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.provider.Provider;

public class ServerBulletEntity extends ServerEntity {
    private final BulletController controller;
    private ServerEntity parent;
    protected Vector2 direction;
    protected int lifetime;
    protected int damage;
    private int index;

    public ServerBulletEntity(BulletEntityType type) {
        super(type, EntityGroup.BULLET);
        controller = new BulletController(this);
        damage = 0;
        index = -1;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public int getLifetime() {
        return lifetime;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setParent(ServerEntity parent) {
        this.parent = parent;
    }

    public ServerEntity getParent() {
        return parent;
    }

    @Override
    public void collisionEnter(ServerEntity other) {
        if (other instanceof ServerLivingEntity && other.getGroup() != parent.getGroup()) {
            ((ServerLivingEntity) other).damage(damage);
            if (!((BulletEntityType) type).piercing().get(Provider.Context.of(this, game.getGametime()))) {
                remove();
            }
        }
    }

    @Override
    public void collisionExit(ServerEntity other) {

    }

    @Override
    public void postSpawn() {
    }

    @Override
    public EntityController<?> getController() {
        return controller;
    }

    @Override
    public void tick(int gametime, float delta) {
        super.tick(gametime, delta);

        if (gametime - getSpawnTime() >= lifetime) {
            remove();
        }
    }
}
