package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.util.Tickable;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public abstract class Entity implements Tickable {
    public static final float COORDINATE_SCALE = 32f;
    public static final Random RANDOM = new Random();
    private final Game game;
    private final EntityType<? extends Entity> type;
    private final UUID uuid;
    private final Vector2 pos;
    private final Vector2 prevPos;
    private final int spawnTime;
    private final Vector2 collider;
    private final Set<UUID> collidingWith;

    public Entity(Game game, EntityType<? extends Entity> type, UUID uuid, Vector2 pos) {
        this.game = game;
        this.type = type;
        this.uuid = uuid;
        this.pos = pos.cpy();
        prevPos = pos.cpy();
        spawnTime = game.getGametime();
        collider = new Vector2(type.scale() * Entity.COORDINATE_SCALE, type.scale() * Entity.COORDINATE_SCALE);
        collidingWith = new HashSet<>();
    }

    public Game getGame() {
        return game;
    }

    public EntityType<? extends Entity> getType() {
        return type;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(float x, float y) {
        pos.set(x, y);
    }

    public Vector2 getPrevPos() {
        return prevPos;
    }

    public void setPrevPos(float x, float y) {
        prevPos.set(x, y);
    }

    public int getSpawnTime() {
        return spawnTime;
    }

    @Override
    public void tick(int gametime, float delta) {
    }

    public void remove() {
        game.getEntityManager().removeEntity(uuid);
    }

    public Vector2 getCollider() {
        return collider;
    }

    public boolean collides(Entity other) {
        return pos.x < other.pos.x + other.collider.x && pos.x + collider.x > other.pos.x && pos.y < other.pos.y + other.collider.y && pos.y + collider.y > other.pos.y;
    }

    public Set<UUID> getCollidingWith() {
        return collidingWith;
    }

    public void setCollidingWith(UUID uuid) {
        if (collidingWith.add(uuid))
            collisionEnter(getGame().getEntityManager().getEntity(uuid));
    }

    public void removeCollidingWith(UUID uuid) {
        if (collidingWith.remove(uuid))
            collisionExit(getGame().getEntityManager().getEntity(uuid));
    }

    public void collisionEnter(Entity other) {
    }

    public void collisionExit(Entity other) {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity entity) {
            return entity.getUuid().equals(getUuid());
        }
        return false;
    }
}
