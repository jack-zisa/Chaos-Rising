package dev.creoii.chaos.server;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.World;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.EntityGroup;
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
            checkTileCollisions();
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

    public void checkTileCollisions() {
        for (Map.Entry<Integer, ObjectList<Entity>> entry : grid.int2ObjectEntrySet()) {
            int key = entry.getKey();

            int x = ((key >>> 16) & 0xffff) - KEY_OFFSET;
            int y = (key & 0xffff) - KEY_OFFSET;

            if (world.getMap().getLayers().get(World.LAYER_GROUND) instanceof TiledMapTileLayer tiledMapTileLayer) {
                TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(x, y);

                if (cell == null || cell.getTile() == null)
                    continue;

                for (Entity entity : entry.getValue()) {
                    if (entity.collidesTile(x, y)) {
                        System.out.println("collides");
                    }
                }
            }
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

    public static Vector2 resolve(Entity entity) {
        float dx = resolveX(entity);
        entity.getPos().x += dx;

        float dy = resolveY(entity);
        entity.getPos().y += dy;

        return new Vector2(dx, dy);
    }

    public static float resolveX(Entity entity) {
        float dx = entity.getVelocity().x;
        if (dx == 0f) return 0f;

        float sign = Math.signum(dx);
        float move = dx;

        float yMin = entity.bottom();
        float yMax = entity.top();

        float edgeX = (sign > 0) ? entity.right() : entity.left();

        int tileY0 = (int) Math.floor(yMin);
        int tileY1 = (int) Math.floor(yMax - 0.0001f);

        int targetTileX = (int) Math.floor(edgeX + dx);

        for (int ty = tileY0; ty <= tileY1; ty++) {
            if (isSolid(targetTileX, ty)) {
                if (sign > 0) {
                    move = Math.min(move, targetTileX - edgeX);
                } else {
                    move = Math.max(move, (targetTileX + 1) - edgeX);
                }
                break;
            }
        }

        return move;
    }

    public static float resolveY(Entity entity) {
        float dy = entity.getVelocity().y;
        if (dy == 0f) return 0f;

        float sign = Math.signum(dy);
        float move = dy;

        float xMin = entity.left();
        float xMax = entity.right();

        float edgeY = (sign > 0) ? entity.top() : entity.bottom();

        int tileX0 = (int) Math.floor(xMin);
        int tileX1 = (int) Math.floor(xMax - 0.0001f);

        int targetTileY = (int) Math.floor(edgeY + dy);

        for (int tx = tileX0; tx <= tileX1; tx++) {
            if (isSolid(tx, targetTileY)) {
                if (sign > 0) {
                    move = Math.min(move, targetTileY - edgeY);
                } else {
                    move = Math.max(move, (targetTileY + 1) - edgeY);
                }
                break;
            }
        }

        return move;
    }

    private static boolean isSolid(int x, int y) {
        return true;
        /*
        if (!(world.getMap().getLayers().get(World.LAYER_GROUND)
            instanceof TiledMapTileLayer layer)) return false;

        TiledMapTileLayer.Cell cell = layer.getCell(x, y);
        if (cell == null || cell.getTile() == null) return false;

        return Boolean.TRUE.equals(cell.getTile().getProperties().get("solid", Boolean.class));*/
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
}
