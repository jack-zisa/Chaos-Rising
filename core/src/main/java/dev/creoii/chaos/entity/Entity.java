package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.World;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.util.Tickable;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;

import javax.annotation.Nullable;
import java.util.*;

public abstract class Entity implements Tickable {
    public static final float COORDINATE_SCALE = 32f;
    private final World world;
    private final EntityType<? extends Entity> type;
    private int id;
    private final Vector2 pos;
    private final Vector2 prevPos;
    private final int spawnTime;
    private final Vector2 collider;
    private final IntSet collidingWith;

    public Entity(World world, EntityType<? extends Entity> type, int id, Vector2 pos) {
        this.world = world;
        this.type = type;
        this.id = id;
        this.pos = pos.cpy();
        prevPos = pos.cpy();
        spawnTime = world.getGame().getGametime();
        collider = new Vector2(type.scale() * .75f, type.scale() * .75f);
        collidingWith = new IntArraySet();
    }

    @Nullable
    public abstract EntityCustomData getCustomPacketData();

    public void reinit(int id, Vector2 pos, Map<String, Object> data) {
        this.id = id;
        this.pos.set(pos);
    }

    public World getWorld() {
        return world;
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

    public boolean isMoving() {
        return !pos.epsilonEquals(prevPos, .001f);
    }

    public void remove() {
        world.getEntityManager().removeEntity(id);
    }

    public boolean collides(Entity other) {
        float ax1 = pos.x + (type.scale() - collider.x) * .5f;
        float ay1 = pos.y + (type.scale() - collider.y) * .5f;
        float ax2 = ax1 + collider.x;
        float ay2 = ay1 + collider.y;

        float bx1 = other.pos.x + (other.type.scale() - other.collider.x) * .5f;
        float by1 = other.pos.y + (other.type.scale() - other.collider.y) * .5f;
        float bx2 = bx1 + other.collider.x;
        float by2 = by1 + other.collider.y;

        return ax1 < bx2 && ax2 > bx1 && ay1 < by2 && ay2 > by1;
    }

    public IntSet getCollidingWith() {
        return collidingWith;
    }

    public void setCollidingWith(Entity entity) {
        if (collidingWith.add(entity.id))
            collisionEnter((Entity) getWorld().getEntityManager().getEntity(entity.getType().group(), entity.id));
    }

    public void removeCollidingWith(Entity entity) {
        if (collidingWith.remove(entity.id))
            collisionExit((Entity) getWorld().getEntityManager().getEntity(entity.getType().group(), entity.id));
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
