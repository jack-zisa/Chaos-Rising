package dev.creoii.chaos.server;

import com.badlogic.gdx.math.Vector2;
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
    private static final int[][] FORWARD_NEIGHBORS = {
        {1, 0}, {1, 1}, {0, 1}, {-1, 1}
    };
    private static final int[][] ALL_NEIGHBORS = {
        {-1,-1},{0,-1},{1,-1},
        {-1, 0},        {1, 0},
        {-1, 1},{0, 1},{1, 1}
    };

    private static final Long2ObjectArrayMap<int[][]> DIRECTION_OFFSETS = createDirectionOffsets();
    private static final int[] COLLISION_MASKS = new int[EntityGroup.values().length];
    public static final int KEY_OFFSET = 32768;
    private final ServerGame game;
    private final Int2ObjectOpenHashMap<ObjectList<Entity>> grid;
    private final Int2ObjectOpenHashMap<ObjectSet<Integer>> collisions;

    public CollisionManager(ServerGame game) {
        this.game = game;
        grid = new Int2ObjectOpenHashMap<>();
        collisions = new Int2ObjectOpenHashMap<>();

        COLLISION_MASKS[EntityGroup.BULLET.ordinal()] = (1 << EntityGroup.ENEMY.ordinal()) | (1 << EntityGroup.CHARACTER.ordinal());
        COLLISION_MASKS[EntityGroup.CHARACTER.ordinal()] = (1 << EntityGroup.BULLET.ordinal()) | (1 << EntityGroup.LOOT_DROP.ordinal());
        COLLISION_MASKS[EntityGroup.ENEMY.ordinal()] = (1 << EntityGroup.BULLET.ordinal());
        COLLISION_MASKS[EntityGroup.LOOT_DROP.ordinal()] = (1 << EntityGroup.CHARACTER.ordinal());
    }

    public void checkCollisions() {
        if (game.getEntityManager().getSize() <= 1)
            return;

        for (ObjectList<Entity> cellEntities : grid.values()) {
            cellEntities.clear();
        }
        grid.clear();

        List<Entity> toCollide = new ArrayList<>();
        for (Int2ObjectOpenHashMap<Entity> entityMap : game.getEntityManager().getAllEntities().values()) {
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
                    Entity b = game.getEntityManager().getEntity(id);
                    if (b == null)
                        continue;
                    it.remove();
                    entity.removeCollidingWith(b);
                    b.removeCollidingWith(entity);
                }
            }
        }

        collisions.clear();
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
