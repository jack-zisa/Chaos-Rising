package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.util.Tickable;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;

import javax.annotation.Nullable;
import java.util.*;

public abstract class Entity implements Tickable {
    public static final float COORDINATE_SCALE = 32f;
    public static final Random RANDOM = new Random();
    private final Game game;
    private final EntityType<? extends Entity> type;
    private int id;
    private final Vector2 pos;
    private final Vector2 prevPos;
    private final int spawnTime;
    private final Vector2 collider;
    private final IntSet collidingWith;

    public Entity(Game game, EntityType<? extends Entity> type, int id, Vector2 pos) {
        this.game = game;
        this.type = type;
        this.id = id;
        this.pos = pos.cpy();
        prevPos = pos.cpy();
        spawnTime = game.getGametime();
        collider = new Vector2(type.scale(), type.scale());
        collidingWith = new IntArraySet();
    }

    @Nullable
    public abstract EntityCustomData getCustomPacketData();

    public void reinit(int id, Vector2 pos, Map<String, Object> data) {
        this.id = id;
        this.pos.set(pos);
    }

    public Game getGame() {
        return game;
    }

    public EntityType<? extends Entity> getType() {
        return type;
    }

    public int getId() {
        return id;
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

    public Vector2 getVelocity() {
        return pos.cpy().sub(prevPos);
    }

    public int getSpawnTime() {
        return spawnTime;
    }

    @Override
    public void tick(int gametime, float delta) {
    }

    public void remove() {
        game.getEntityManager().removeEntity(id);
    }

    public Vector2 getCollider() {
        return collider;
    }

    public boolean collides(Entity other) {
        return pos.x < other.pos.x + other.collider.x && pos.x + collider.x > other.pos.x && pos.y < other.pos.y + other.collider.y && pos.y + collider.y > other.pos.y;
    }

    public IntSet getCollidingWith() {
        return collidingWith;
    }

    public void setCollidingWith(int id) {
        if (collidingWith.add(id))
            collisionEnter((Entity) getGame().getEntityManager().getEntity(id));
    }

    public void removeCollidingWith(int id) {
        if (collidingWith.remove(id))
            collisionExit((Entity) getGame().getEntityManager().getEntity(id));
    }

    public void collisionEnter(Entity other) {
    }

    public void collisionExit(Entity other) {
    }

    public boolean canMove() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity entity) {
            return entity.id == id;
        }
        return false;
    }
}
