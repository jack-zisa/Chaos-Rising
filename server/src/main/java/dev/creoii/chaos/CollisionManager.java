package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.entity.ServerBulletEntity;
import dev.creoii.chaos.entity.ServerEntity;

import java.util.*;

public class CollisionManager {
    private static final int[][] FORWARD_NEIGHBORS = {
            {1, 0}, {1, 1}, {0, 1}, {-1, 1}
    };
    private static final boolean[][] COLLISION_MATRIX = new boolean[ServerEntity.Group.values().length][ServerEntity.Group.values().length];
    public static final int KEY_OFFSET = 32768;
    private float cellSize = ServerEntity.COORDINATE_SCALE;
    private final Main main;
    private final ObjectMap<Integer, Array<ServerEntity>> grid;

    public CollisionManager(Main main) {
        this.main = main;
        grid = new ObjectMap<>();

        COLLISION_MATRIX[ServerEntity.Group.BULLET.ordinal()][ServerEntity.Group.ENEMY.ordinal()] = true;
        COLLISION_MATRIX[ServerEntity.Group.BULLET.ordinal()][ServerEntity.Group.CHARACTER.ordinal()] = true;
        COLLISION_MATRIX[ServerEntity.Group.ENEMY.ordinal()][ServerEntity.Group.BULLET.ordinal()] = true;
        COLLISION_MATRIX[ServerEntity.Group.CHARACTER.ordinal()][ServerEntity.Group.BULLET.ordinal()] = true;
        COLLISION_MATRIX[ServerEntity.Group.CHARACTER.ordinal()][ServerEntity.Group.OTHER.ordinal()] = true;
        COLLISION_MATRIX[ServerEntity.Group.OTHER.ordinal()][ServerEntity.Group.CHARACTER.ordinal()] = true;
    }

    public float getCellSize() {
        return cellSize;
    }

    public void setCellSize(float cellSize) {
        this.cellSize = cellSize;
    }

    public ObjectMap<Integer, Array<ServerEntity>> getGrid() {
        return grid;
    }

    public void checkCollisions() {
        for (Array<ServerEntity> cellEntities : grid.values()) {
            cellEntities.clear();
        }
        grid.clear();

        for (ServerEntity entity : main.getGame().getEntityManager().getEntities().values()) {
            int x = Math.round(entity.getPos().x / cellSize);
            int y = Math.round(entity.getPos().y / cellSize);

            int key = ((x + KEY_OFFSET) << 16) | ((y + KEY_OFFSET) & 0xffff);
            Array<ServerEntity> entities = grid.get(key);
            if (entities == null) {
                entities = new Array<>();
                grid.put(key, entities);
            }
            entities.add(entity);
        }

        Map<UUID, Set<UUID>> collisions = new HashMap<>();

        for (ObjectMap.Entry<Integer, Array<ServerEntity>> entry : grid.entries()) {
            Array<ServerEntity> entities = entry.value;

            for (int i = 0; i < entities.size; ++i) {
                ServerEntity a = entities.get(i);
                for (int j = i + 1; j < entities.size; ++j) {
                    ServerEntity b = entities.get(j);
                    if (COLLISION_MATRIX[a.getGroup().ordinal()][b.getGroup().ordinal()] && a.getColliderRect().overlaps(b.getColliderRect())) {
                        collisions.computeIfAbsent(a.getUuid(), k -> new HashSet<>()).add(b.getUuid());
                        collisions.computeIfAbsent(b.getUuid(), k -> new HashSet<>()).add(a.getUuid());

                        a.setCollidingWith(b.getUuid());
                        b.setCollidingWith(a.getUuid());
                    }
                }
            }

            int x = (entry.key >> 16) - KEY_OFFSET;
            int y = (entry.key & 0xffff) - KEY_OFFSET;

            for (ServerEntity a : entities) {
                int[][] neighborDirs = a instanceof ServerBulletEntity bullet ? getBulletForwardNeighbors(bullet) : FORWARD_NEIGHBORS;

                for (int[] dir : neighborDirs) {
                    int neighborKey = ((x + dir[0] + KEY_OFFSET) << 16) | ((y + dir[1] + KEY_OFFSET) & 0xffff);
                    Array<ServerEntity> neighbors = grid.get(neighborKey);
                    if (neighbors == null) continue;

                    for (ServerEntity b : neighbors) {
                        if (a == b) continue;

                        if (COLLISION_MATRIX[a.getGroup().ordinal()][b.getGroup().ordinal()] && a.getColliderRect().overlaps(b.getColliderRect())) {
                            collisions.computeIfAbsent(a.getUuid(), k -> new HashSet<>()).add(b.getUuid());
                            collisions.computeIfAbsent(b.getUuid(), k -> new HashSet<>()).add(a.getUuid());

                            a.setCollidingWith(b.getUuid());
                            b.setCollidingWith(a.getUuid());
                        }
                    }
                }
            }
        }

        for (ServerEntity entity : main.getGame().getEntityManager().getEntities().values()) {
            Set<UUID> currentlyColliding = collisions.getOrDefault(entity.getUuid(), Collections.emptySet());

            Set<UUID> previousColliding = new HashSet<>(entity.getCollidingWith());
            for (UUID uuid : previousColliding) {
                if (!currentlyColliding.contains(uuid)) {
                    entity.removeCollidingWith(uuid);
                }
            }
        }
    }

    private static int[][] getBulletForwardNeighbors(ServerBulletEntity bullet) {
        Vector2 dir = bullet.getDirection();
        if (dir.isZero())
            return new int[0][0];

        dir = dir.cpy().nor();

        int dx = Math.round(dir.x);
        int dy = Math.round(dir.y);

        Array<int[]> offsets = new Array<>();

        offsets.add(new int[]{dx, dy});
        if (dx != 0 && dy != 0) {
            offsets.add(new int[]{dx, 0});
            offsets.add(new int[]{0, dy});
        }

        return offsets.toArray(int[].class);
    }
}
