package dev.creoii.chaos.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.util.Positionable;
import dev.creoii.chaos.util.Tickable;

import java.util.*;

public abstract class Entity implements Positionable, Tickable {
    public static final float COORDINATE_SCALE = 32f;
    protected static final Random RANDOM = new Random();
    // template (non-active) fields
    private final float scale;
    private final String textureId;
    private final Vector2 collider;
    private final Group group;
    private boolean moving;
    private long spawnTime;

    // active fields
    protected Game game;
    protected Vector2 pos;
    protected Vector2 centerPos;
    protected Rectangle colliderRect;
    protected Set<UUID> collidingWith;
    protected UUID uuid;
    protected Sprite sprite;

    public Entity(String textureId, float scale, Vector2 collider, Group group) {
        this.scale = scale;
        this.textureId = textureId;
        this.collider = collider;
        this.group = group;
        moving = false;
        spawnTime = -1;
    }

    public abstract Entity create(Game game, UUID uuid, Vector2 pos);

    public abstract EntityController<?> getController();

    public abstract void collisionEnter(Entity other);

    public abstract void collisionExit(Entity other);

    public abstract void postSpawn();

    public void tick(int gametime, float delta) {
        if (getController() != null) {
            getController().control(gametime, delta);
        }
    }

    public float getScale() {
        return scale * COORDINATE_SCALE;
    }

    public String getTextureId() {
        return textureId;
    }

    public Vector2 getCollider() {
        return collider;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getColliderRect() {
        if (pos == null)
            return null;
        colliderRect.setPosition(pos);
        return colliderRect;
    }

    public Group getGroup() {
        return group;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getCenterPos() {
        centerPos.set(getPos()).add(COORDINATE_SCALE / 2f, COORDINATE_SCALE / 2f);
        sprite.setCenter(centerPos.x, centerPos.y);
        return centerPos;
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

    public Entity spawn(Game game, UUID uuid, Vector2 pos, Map<String, Object> customData) {
        Entity entity = create(game, uuid, pos);
        entity.game = game;
        entity.uuid = uuid;
        entity.pos = pos;
        entity.centerPos = new Vector2();
        entity.colliderRect = new Rectangle();
        entity.colliderRect.setPosition(pos);
        entity.colliderRect.setWidth(collider.x * getScale());
        entity.colliderRect.setHeight(collider.y * getScale());
        entity.getCenterPos();
        entity.collidingWith = new HashSet<>();
        entity.spawnTime = game.getGametime();
        entity.postSpawn();
        return entity;
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

    public enum Group {
        CHARACTER,
        ENEMY,
        BULLET,
        OTHER
    }
}
