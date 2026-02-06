package dev.creoii.chaos.server;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.world.tile.Tile;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.util.*;
import java.util.function.BiFunction;

public class CollisionManager {
    private static final int[][] ALL_NEIGHBORS = {
        {-1,-1},{0,-1},{1,-1},
        {-1, 0},        {1, 0},
        {-1, 1},{0, 1},{1, 1}
    };

    private static final Long2ObjectArrayMap<int[][]> DIRECTION_OFFSETS = createDirectionOffsets();
    private static final int[] COLLISION_MASKS = new int[EntityGroup.values().length];
    public static final int KEY_OFFSET = 32768;
    private static final float PADDING = Entity.COORDINATE_SCALE / 8f;
    private static final float STEP_SIZE = Entity.COORDINATE_SCALE * .2f;
    private static final float EPS = .001f;
    private final ServerWorld world;
    private final ObjectList<Entity> toCollide;
    private final Int2ObjectOpenHashMap<ObjectList<Entity>> grid;
    private final Int2ObjectOpenHashMap<ObjectSet<Integer>> collisions;

    public CollisionManager(ServerWorld world) {
        this.world = world;
        toCollide = new ObjectArrayList<>();
        grid = new Int2ObjectOpenHashMap<>();
        collisions = new Int2ObjectOpenHashMap<>();

        COLLISION_MASKS[EntityGroup.BULLET.ordinal()] = (1 << EntityGroup.ENEMY.ordinal()) | (1 << EntityGroup.CHARACTER.ordinal());
        COLLISION_MASKS[EntityGroup.CHARACTER.ordinal()] = (1 << EntityGroup.BULLET.ordinal()) | (1 << EntityGroup.LOOT_DROP.ordinal());
        COLLISION_MASKS[EntityGroup.ENEMY.ordinal()] = (1 << EntityGroup.BULLET.ordinal());
        COLLISION_MASKS[EntityGroup.LOOT_DROP.ordinal()] = (1 << EntityGroup.CHARACTER.ordinal());
    }

    public void checkCollisions() {
        if (world.getEntityManager().getSize() > 0) {
            start();
            if (world.getEntityManager().getSize() > 1) {
                checkEntityCollisions();
            }
            end();
        }
    }

    public void start() {
        for (ObjectList<Entity> cellEntities : grid.values()) {
            cellEntities.clear();
        }
        grid.clear();

        toCollide.clear();

        for (Int2ObjectOpenHashMap<Entity> entityMap : world.getEntityManager().getAllEntities().values()) {
            toCollide.addAll(entityMap.values());
        }

        for (Entity entity : toCollide) {
            int x = (int) Math.floor(entity.getPos().x / Entity.COORDINATE_SCALE);
            int y = (int) Math.floor(entity.getPos().y / Entity.COORDINATE_SCALE);

            int key = ((x + KEY_OFFSET) << 16) | ((y + KEY_OFFSET) & 0xffff);
            ObjectList<Entity> entities = grid.get(key);
            if (entities == null) {
                entities = new ObjectArrayList<>();
                grid.put(key, entities);
            }
            entities.add(entity);
        }
    }

    public void checkEntityCollisions() {
        for (Map.Entry<Integer, ObjectList<Entity>> entry : grid.int2ObjectEntrySet()) {
            ObjectList<Entity> entities = entry.getValue();

            int size = entities.size();
            for (int i = 0; i < size; ++i) {
                Entity a = entities.get(i);
                for (int j = i + 1; j < size; ++j) {
                    Entity b = entities.get(j);

                    if (!a.isMoving() && !b.isMoving())
                        continue;

                    if (checkMask(a, b) && a.collides(b)) {
                        collisions.computeIfAbsent(a.getId(), _ -> new ObjectArraySet<>()).add(b.getId());
                        collisions.computeIfAbsent(b.getId(), _ -> new ObjectArraySet<>()).add(a.getId());

                        a.setCollidingWith(b);
                        b.setCollidingWith(a);
                    }
                }
            }

            int x = (entry.getKey() >> 16) - KEY_OFFSET;
            int y = (entry.getKey() & 0xffff) - KEY_OFFSET;

            for (Entity a : entities) {
                int[][] neighborDirs = a instanceof BulletEntity bullet ? getBulletForwardNeighbors(bullet) : ALL_NEIGHBORS;

                for (int[] dir : neighborDirs) {
                    int neighborKey = ((x + dir[0] + KEY_OFFSET) << 16) | ((y + dir[1] + KEY_OFFSET) & 0xffff);
                    ObjectList<Entity> neighbors = grid.get(neighborKey);
                    if (neighbors == null)
                        continue;

                    for (Entity b : neighbors) {
                        if (a == b)
                            continue;

                        if (!a.isMoving() && !b.isMoving())
                            continue;

                        if (checkMask(a, b) && a.collides(b)) {
                            collisions.computeIfAbsent(a.getId(), _ -> new ObjectArraySet<>()).add(b.getId());
                            collisions.computeIfAbsent(b.getId(), _ -> new ObjectArraySet<>()).add(a.getId());

                            a.setCollidingWith(b);
                            b.setCollidingWith(a);
                        }
                    }
                }
            }
        }

        for (Entity entity : toCollide) {
            Set<Integer> currentlyColliding = collisions.getOrDefault(entity.getId(), new ObjectArraySet<>());
            Iterator<Integer> it = entity.getCollidingWith().iterator();
            while (it.hasNext()) {
                int id = it.next();
                if (!currentlyColliding.contains(id)) {
                    Entity b = world.getEntityManager().getEntity(id);
                    if (b == null)
                        continue;

                    it.remove();
                    entity.removeCollidingWith(b);
                    b.removeCollidingWith(entity);
                }
            }
        }
    }

    public void end() {
        collisions.clear();
    }

    private static int toTile(float worldCoord) {
        return MathUtils.floor(worldCoord / Entity.COORDINATE_SCALE);
    }

    public CollisionResult resolve(Entity entity) {
        entity.collidingLeft = false;
        entity.collidingRight = false;
        entity.collidingUp = false;
        entity.collidingDown = false;

        Vector2 pos = entity.getPos().cpy();
        Vector2 vel = entity.getVelocity();

        if (entity.getTileCollisionType() == Entity.TileCollisionType.PASS)
            return new CollisionResult(pos.add(vel), false, false);

        boolean hitX = false;
        boolean hitY = false;

        float newX = pos.x;
        float newY = pos.y;

        if (vel.x != 0f) {
            float x = resolveX(entity, pos, vel.x);
            hitX = (x != pos.x + vel.x);
            newX = x;
        }

        pos.x = newX;

        if (vel.y != 0f) {
            float y = resolveY(entity, pos, vel.y);
            hitY = (y != pos.y + vel.y);
            newY = y;
        }

        return new CollisionResult(new Vector2(newX, newY), hitX, hitY);
    }

    private float resolveX(Entity entity, Vector2 pos, float dx) {
        float remaining = Math.abs(dx);
        float dir = Math.signum(dx);

        float size = entity.getType().scale();

        float x = pos.x;

        while (remaining > 0f) {
            float step = Math.min(STEP_SIZE, remaining);

            if (dir != 0f) {
                x += dir * step;

                float left = x + (PADDING / 2f);
                float right = x + size - (PADDING / 2f);
                float bottom = pos.y + (PADDING / 2f);
                float top = pos.y + size - (PADDING / 2f);

                int tileX = toTile(dir > 0 ? right : left);
                int yStart = toTile(bottom);
                int yEnd = toTile(top - EPS);

                for (int y = yStart; y <= yEnd; y++) {
                    Tile tile = world.getGround(tileX, y);
                    if (tile != null && tile.isSolid()) {
                        if (entity.getTileCollisionType() == Entity.TileCollisionType.REMOVE) {
                            entity.remove();
                            return 0f;
                        }

                        if (dir > 0) {
                            x = tileX * Entity.COORDINATE_SCALE - size + (PADDING / 2f);
                            entity.collidingRight = true;
                        } else {
                            x = (tileX + 1) * Entity.COORDINATE_SCALE - (PADDING / 2f);
                            entity.collidingLeft = true;
                        }

                        remaining = 0f;
                        break;
                    }
                }
            }

            remaining -= step;
        }

        return x;
    }

    private float resolveY(Entity entity, Vector2 pos, float dy) {
        float remaining = Math.abs(dy);
        float dir = Math.signum(dy);

        float size = entity.getType().scale();

        float y = pos.y;

        while (remaining > 0f) {
            float step = Math.min(STEP_SIZE, remaining);

            if (dir != 0f) {
                y += dir * step;

                float left = pos.x + (PADDING / 2f);
                float right = pos.x + size - (PADDING / 2f);
                float bottom = y + (PADDING / 2f);
                float top = y + size - (PADDING / 2f);

                int tileY = toTile(dir > 0 ? top : bottom);
                int xStart = toTile(left);
                int xEnd = toTile(right - EPS);

                for (int x = xStart; x <= xEnd; x++) {
                    Tile tile = world.getGround(x, tileY);
                    if (tile != null && tile.isSolid()) {
                        if (entity.getTileCollisionType() == Entity.TileCollisionType.REMOVE) {
                            entity.remove();
                            return 0f;
                        }

                        if (dir > 0) {
                            y = tileY * Entity.COORDINATE_SCALE - size + (PADDING / 2f);
                            entity.collidingUp = true;
                        } else {
                            y = (tileY + 1) * Entity.COORDINATE_SCALE - (PADDING / 2f);
                            entity.collidingDown = true;
                        }

                        remaining = 0f;
                        break;
                    }
                }
            }

            remaining -= step;
        }

        return y;
    }

    private static int[][] getBulletForwardNeighbors(BulletEntity bullet) {
        Vector2 dir = bullet.getDirection();
        if (dir.isZero())
            return new int[0][0];

        int dx = (int) Math.signum(dir.x);
        int dy = (int) Math.signum(dir.y);

        long key = ((long) (dx + 1) << 2) | (dy + 1);
        return DIRECTION_OFFSETS.getOrDefault(key, new int[0][0]);
    }


    private static Long2ObjectArrayMap<int[][]> createDirectionOffsets() {
        Long2ObjectArrayMap<int[][]> map = new Long2ObjectArrayMap<>();

        BiFunction<Integer, Integer, Long> key = (x, y) -> ((long) (x + 1) << 2) | (y + 1);

        map.put((long) key.apply(1, 0), new int[][]{   // East
            {1, 0}, {1, 1}, {1, -1}});
        map.put((long) key.apply(-1, 0), new int[][]{  // West
            {-1, 0}, {-1, 1}, {-1, -1}});
        map.put((long) key.apply(0, 1), new int[][]{   // North
            {0, 1}, {1, 1}, {-1, 1}});
        map.put((long) key.apply(0, -1), new int[][]{  // South
            {0, -1}, {1, -1}, {-1, -1}});

        map.put((long) key.apply(1, 1), new int[][]{   // NE
            {1, 1}, {1, 0}, {0, 1}});
        map.put((long) key.apply(-1, 1), new int[][]{  // NW
            {-1, 1}, {-1, 0}, {0, 1}});
        map.put((long) key.apply(1, -1), new int[][]{  // SE
            {1, -1}, {1, 0}, {0, -1}});
        map.put((long) key.apply(-1, -1), new int[][]{ // SW
            {-1, -1}, {-1, 0}, {0, -1}});

        return map;
    }

    private static boolean checkMask(Entity a, Entity b) {
        return (COLLISION_MASKS[a.getType().group().ordinal()] & (1 << b.getType().group().ordinal())) != 0;
    }

    public record CollisionResult(
        Vector2 position,
        boolean hitX,
        boolean hitY
    ) {}
}
