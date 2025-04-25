package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.network.packet.util.EntityGroup;
import dev.creoii.chaos.util.Positionable;
import dev.creoii.chaos.util.Tickable;

import java.util.*;

public abstract class Entity implements Positionable, Tickable {
    public static final float COORDINATE_SCALE = 32f;
    protected static final Random RANDOM = new Random();
    protected final EntityType<?> type;
    private final EntityGroup group;
    protected long spawnTime;
    protected ServerGame game;
    protected Vector2 pos;
    protected Vector2 centerPos;
    protected Rectangle colliderRect;
    protected Set<UUID> collidingWith;
    protected UUID uuid;

    public Entity(EntityType<?> type, EntityGroup group) {
        this.type = type;
        this.group = group;
        spawnTime = -1;
    }

    public abstract EntityController<?> getController();

    public abstract void collisionEnter(Entity other);

    public abstract void collisionExit(Entity other);

    public abstract void postSpawn();

    public void tick(int gametime, float delta) {
        if (getController() != null) {
            getController().control(gametime, delta);
        }
    }

    public EntityType<?> getType() {
        return type;
    }

    public float getScale() {
        return type.scale() * COORDINATE_SCALE;
    }

    public String getTextureId() {
        return type.textureId();
    }

    public Rectangle getColliderRect() {
        if (pos == null)
            return null;
        colliderRect.setPosition(pos);
        return colliderRect;
    }

    public EntityGroup getGroup() {
        return group;
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    public ServerGame getGame() {
        return game;
    }

    @Override
    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getCenterPos() {
        return centerPos.set(getPos()).add(COORDINATE_SCALE / 4f, COORDINATE_SCALE / 4f);
    }

    public Set<UUID> getCollidingWith() {
        return collidingWith;
    }

    public boolean isCollidingWith(UUID uuid) {
        return collidingWith.contains(uuid);
    }

    public void setCollidingWith(UUID uuid) {
        if (collidingWith.add(uuid))
            collisionEnter(game.getEntityManager().getEntity(uuid));
    }

    public void removeCollidingWith(UUID uuid) {
        if (collidingWith.remove(uuid))
            collisionExit(game.getEntityManager().getEntity(uuid));
    }

    public UUID getUuid() {
        return uuid;
    }

    public void remove() {
        game.getEntityManager().removeEntity(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity entity) {
            return entity.getUuid().equals(getUuid());
        }
        return false;
    }

}
