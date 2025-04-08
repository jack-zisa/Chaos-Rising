package dev.creoii.chaos;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.entity.Entity;

public class CollisionManager {
    private static final int[][] FORWARD_NEIGHBORS = {
            {1, 0}, {1, 1}, {0, 1}, {-1, 1}
    };
    public static final int KEY_OFFSET = 32768;
    private float cellSize = Entity.COORDINATE_SCALE;
    private final Main main;
    private final ObjectMap<Integer, Array<Entity>> grid;

    public CollisionManager(Main main) {
        this.main = main;
        grid = new ObjectMap<>();
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
        for (Array<Entity> cellEntities : grid.values()) { // improve grid clearing with a boolean flag storing in the integer key
            cellEntities.clear();
        }

        for (Entity entity : main.getGame().getEntityManager().getActiveEntities().values()) {
            int x = Math.round(entity.getPos().x / cellSize);
            int y = Math.round(entity.getPos().y / cellSize);

            int key = ((x + KEY_OFFSET) << 16) | ((y + KEY_OFFSET) & 0xffff);
            Array<Entity> cellEntities = grid.get(key);
            if (cellEntities == null) {
                cellEntities = new Array<>();
                grid.put(key, cellEntities);
            }
            cellEntities.add(entity);
        }

        for (ObjectMap.Entry<Integer, Array<Entity>> entry : grid.entries()) {
            Array<Entity> entities = entry.value;

            for (int i = 0; i < entities.size; ++i) {
                Entity a = entities.get(i);
                for (int j = i + 1; j < entities.size; ++j) {
                    Entity b = entities.get(j);
                    if (a.getColliderRect().overlaps(b.getColliderRect())) {
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
                        if (a != b && a.getColliderRect().overlaps(b.getColliderRect())) {
                            a.collide(b);
                            b.collide(a);
                        }
                    }
                }
            }
        }
    }
}
