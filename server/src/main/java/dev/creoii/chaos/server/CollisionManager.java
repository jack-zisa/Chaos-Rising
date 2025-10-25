package dev.creoii.chaos.server;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import dev.creoii.chaos.entity.BulletEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.EntityGroup;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.util.*;

public class CollisionManager {
    private static final int[][] FORWARD_NEIGHBORS = {
        {1, 0}, {1, 1}, {0, 1}, {-1, 1}
    };
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
        game.getEntityManager().getAllEntities().values().forEach(uuidEntityMap -> toCollide.addAll(uuidEntityMap.values()));

        for (Entity entity : toCollide) {
            int x = Math.round(entity.getPos().x / Entity.COORDINATE_SCALE);
            int y = Math.round(entity.getPos().y / Entity.COORDINATE_SCALE);

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

            for (int i = 0; i < entities.size(); ++i) {
                Entity a = entities.get(i);
                for (int j = i + 1; j < entities.size(); ++j) {
                    Entity b = entities.get(j);
                    if (checkMask(a, b) && a.collides(b)) {
                        collisions.computeIfAbsent(a.getId(), _ -> new ObjectArraySet<>()).add(b.getId());
                        collisions.computeIfAbsent(b.getId(), _ -> new ObjectArraySet<>()).add(a.getId());

                        a.setCollidingWith(b.getId());
                        b.setCollidingWith(a.getId());
                    }
                }
            }

            int x = (entry.getKey() >> 16) - KEY_OFFSET;
            int y = (entry.getKey() & 0xffff) - KEY_OFFSET;

            for (Entity a : entities) {
                int[][] neighborDirs = a instanceof BulletEntity bullet ? getBulletForwardNeighbors(bullet) : FORWARD_NEIGHBORS;

                for (int[] dir : neighborDirs) {
                    int neighborKey = ((x + dir[0] + KEY_OFFSET) << 16) | ((y + dir[1] + KEY_OFFSET) & 0xffff);
                    ObjectList<Entity> neighbors = grid.get(neighborKey);
                    if (neighbors == null)
                        continue;

                    for (Entity b : neighbors) {
                        if (a == b)
                            continue;

                        if (checkMask(a, b) && a.collides(b)) {
                            collisions.computeIfAbsent(a.getId(), _ -> new ObjectArraySet<>()).add(b.getId());
                            collisions.computeIfAbsent(b.getId(), _ -> new ObjectArraySet<>()).add(a.getId());

                            a.setCollidingWith(b.getId());
                            b.setCollidingWith(a.getId());
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
                    entity.removeCollidingWith(id);
                    b.removeCollidingWith(entity.getId());
                }
            }
        }

        collisions.clear();
    }

    private static int[][] getBulletForwardNeighbors(BulletEntity bullet) {
        Vector2 dir = bullet.getDirection();
        if (dir.isZero())
            return new int[0][0];

        dir = dir.cpy().nor();

        int dx = Math.round(dir.x);
        int dy = Math.round(dir.y);

        Array<int[]> offsets = new Array<>();

        offsets.add(new int[]{dx, dy});

        if (dx != 0)
            offsets.add(new int[]{dx, 0});
        if (dy != 0)
            offsets.add(new int[]{0, dy});

        return offsets.toArray(int[].class);
    }

    private static boolean checkMask(Entity a, Entity b) {
        return (COLLISION_MASKS[a.getType().group().ordinal()] & (1 << b.getType().group().ordinal())) != 0;
    }
}
