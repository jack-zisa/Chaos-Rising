package dev.creoii.chaos;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.entity.Entity;

import java.util.Arrays;

public class CollisionManager {
    private static final int[][] FORWARD_NEIGHBORS = {
            {1, 0}, {1, 1}, {0, 1}, {-1, 1}
    };
    private static final boolean[][] COLLISION_MATRIX = new boolean[Entity.Group.values().length][Entity.Group.values().length];
    public static final int KEY_OFFSET = 32768;
    private float cellSize = Entity.COORDINATE_SCALE;
    private final Main main;
    private final ObjectMap<Integer, Array<Entity>> grid;

    public CollisionManager(Main main) {
        this.main = main;
        grid = new ObjectMap<>();

        COLLISION_MATRIX[Entity.Group.BULLET.ordinal()][Entity.Group.ENEMY.ordinal()] = true;
        COLLISION_MATRIX[Entity.Group.BULLET.ordinal()][Entity.Group.CHARACTER.ordinal()] = true;
        COLLISION_MATRIX[Entity.Group.ENEMY.ordinal()][Entity.Group.BULLET.ordinal()] = true;
        COLLISION_MATRIX[Entity.Group.CHARACTER.ordinal()][Entity.Group.BULLET.ordinal()] = true;
    }

    public float getCellSize() {
        return cellSize;
    }

    public void setCellSize(float cellSize) {
        this.cellSize = cellSize;
    }

    public ObjectMap<Integer, Array<Entity>> getGrid() {
        return grid;
    }

    public void checkCollisions() {
        for (Array<Entity> cellEntities : grid.values()) {
            cellEntities.clear();
        }
        grid.clear();

        for (Entity entity : main.getGame().getEntityManager().getActiveEntities().values()) {
            int x = Math.round(entity.getPos().x / cellSize);
            int y = Math.round(entity.getPos().y / cellSize);

            int key = ((x + KEY_OFFSET) << 16) | ((y + KEY_OFFSET) & 0xffff);
            Array<Entity> entities = grid.get(key);
            if (entities == null) {
                entities = new Array<>();
                grid.put(key, entities);
            }
            entities.add(entity);
        }

        for (ObjectMap.Entry<Integer, Array<Entity>> entry : grid.entries()) {
            Array<Entity> entities = entry.value;

            for (int i = 0; i < entities.size; ++i) {
                Entity a = entities.get(i);
                for (int j = i + 1; j < entities.size; ++j) {
                    Entity b = entities.get(j);
                    if (COLLISION_MATRIX[a.getGroup().ordinal()][b.getGroup().ordinal()] && a.getColliderRect().overlaps(b.getColliderRect())) {
                        a.collide(b);
                        b.collide(a);
                    }
                }
            }

            int x = (entry.key >> 16) - KEY_OFFSET;
            int y = (entry.key & 0xffff) - KEY_OFFSET;

            for (int[] neighbor : FORWARD_NEIGHBORS) {
                Array<Entity> neighbors = grid.get(((x + neighbor[0] + KEY_OFFSET) << 16) | ((y + neighbor[1] + KEY_OFFSET) & 0xffff));
                if (neighbors == null)
                    continue;

                for (int i = 0; i < entities.size; ++i) {
                    Entity a = entities.get(i);
                    for (int j = 0; j < neighbors.size; ++j) {
                        Entity b = neighbors.get(j);
                        if (COLLISION_MATRIX[a.getGroup().ordinal()][b.getGroup().ordinal()] && a != b && a.getColliderRect().overlaps(b.getColliderRect())) {
                            a.collide(b);
                            b.collide(a);
                        }
                    }
                }
            }
        }
    }
}
