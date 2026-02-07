package dev.creoii.chaos.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.World;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.util.Tickable;
import dev.creoii.chaos.world.tile.Tile;
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
    private final Vector2 velocity;
    private final int spawnTime;
    private final Vector2 collider;
    private final IntSet collidingWith;
    public boolean collidingLeft;
    public boolean collidingRight;
    public boolean collidingUp;
    public boolean collidingDown;

    public Entity(World world, EntityType<? extends Entity> type, int id, Vector2 pos) {
        this.world = world;
        this.type = type;
        this.id = id;
        this.pos = pos.cpy();
        prevPos = pos.cpy();
        velocity = Vector2.Zero.cpy();
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
        return velocity;
    }

    public void setVelocity(float x, float y) {
        velocity.set(x, y);
    }

    public float getWidth() {
        return collider.x;
    }

    public float getHeight() {
        return collider.y;
    }

    public int getSpawnTime() {
        return spawnTime;
    }

    public boolean isMoving() {
        return !pos.epsilonEquals(prevPos, .001f);
    }

    public boolean isColliding() {
        return collidingLeft || collidingRight || collidingUp || collidingDown;
    }

    public void remove() {
        world.getEntityManager().removeEntity(id);
    }

    public boolean collides(Entity other) {
        Rectangle a = new Rectangle();
        Rectangle b = new Rectangle();

        getAABB(a);
        other.getAABB(b);

        return a.overlaps(b);
    }

    private void getAABB(Rectangle out) {
        float x1 = pos.x + (type.scale() - collider.x) * .5f;
        float y1 = pos.y + (type.scale() - collider.y) * .5f;
        out.set(x1, y1, collider.x, collider.y);
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
        return !velocity.isZero();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity entity) {
            return entity.id == id;
        }
        return false;
    }

    @Nullable
    public Tile getTileOn() {
        int x = Math.round(getPos().x / Entity.COORDINATE_SCALE);
        int y = Math.round(getPos().y / Entity.COORDINATE_SCALE);
        return world.getGround(x, y);
    }

    public abstract TileCollisionType getTileCollisionType();

    public enum TileCollisionType {
        BLOCK,
        REMOVE,
        PASS
    }
}
