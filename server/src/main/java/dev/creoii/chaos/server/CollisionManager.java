package dev.creoii.chaos.server;

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
    private static final float SKIN = .001f;
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

    public Vector2 resolve(Entity entity) {
        Vector2 move = resolveXY(entity);
        entity.getPos().y += move.y;
        return move;
    }

    public Vector2 resolveXY(Entity entity) {
        Vector2 move = entity.getVelocity().cpy();

        if (move.x != 0f) {
            float signX = Math.signum(move.x);
            float edgeX = signX > 0f ? entity.right() : entity.left();
            float targetX = edgeX + move.x;

            int tileX = (int) Math.floor((signX > 0f ? targetX : targetX - .0001f) / Entity.COORDINATE_SCALE);

            int y0 = (int) Math.floor(entity.bottom() / Entity.COORDINATE_SCALE);
            int y1 = (int) Math.floor((entity.top() - .0001f) / Entity.COORDINATE_SCALE);

            for (int ty = y0; ty <= y1; ty++) {
                if (isSolid(tileX, ty)) {
                    float tileEdgeWorldX = signX > 0f ? tileX * Entity.COORDINATE_SCALE : (tileX + 1f) * Entity.COORDINATE_SCALE;
                    move.x = signX > 0f
                        ? tileEdgeWorldX - edgeX - SKIN
                        : tileEdgeWorldX - edgeX + SKIN;
                    break;
                }
            }

            if (Math.signum(move.x) != signX) move.x = 0;
        }

        entity.getPos().x += move.x;

        if (move.y != 0f) {
            float signY = Math.signum(move.y);
            float edgeY = signY > 0f ? entity.top() : entity.bottom();
            float targetY = edgeY + move.y;

            int tileY = (int) Math.floor((signY > 0f ? targetY : targetY - .0001f) / Entity.COORDINATE_SCALE);

            int x0 = (int) Math.floor(entity.left() / Entity.COORDINATE_SCALE);
            int x1 = (int) Math.floor((entity.right() - .0001f) / Entity.COORDINATE_SCALE);

            for (int tx = x0; tx <= x1; tx++) {
                if (isSolid(tx, tileY)) {
                    float tileEdgeWorldY = signY > 0f ? tileY * Entity.COORDINATE_SCALE : (tileY + 1f) * Entity.COORDINATE_SCALE;
                    move.y = signY > 0f
                        ? tileEdgeWorldY - edgeY - SKIN
                        : tileEdgeWorldY - edgeY + SKIN;
                    break;
                }
            }

            if (Math.signum(move.y) != signY) move.y = 0;
        }

        return move;
    }

    private boolean isSolid(int x, int y) {
        Tile tile = world.getGround(x, y);

        if (tile == null)
            return false;

        return "stone".equals(tile.id());
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
