package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class CollisionManager {
    private final Main main;
    private final Map<Cell, Array<Entity>> grid; // like a chunk system

    public CollisionManager(Main main) {
        this.main = main;
        this.grid = new HashMap<>();
    }

    public Cell getCell(Vector2 pos) {
        return new Cell((int)(pos.x / 32f), (int)(pos.y / 32f));
    }

    public void checkCollisions() {
        for (Entity entity : main.getGame().getEntityManager().getActiveEntities().values()) {
            if (entity.isMoving()) {
                Cell cell = getCell(entity.getPos());
                grid.computeIfAbsent(cell, k -> new Array<>()).add(entity);
            }
        }

        for (Map.Entry<Cell, Array<Entity>> entry : grid.entrySet()) {
            Cell cell = entry.getKey();
            Array<Entity> entitiesInCell = entry.getValue();

            for (int i = 0; i < entitiesInCell.size; i++) {
                Entity a = entitiesInCell.get(i);
                for (int j = i + 1; j < entitiesInCell.size; j++) {
                    Entity b = entitiesInCell.get(j);
                    if (a.getColliderRect().overlaps(b.getColliderRect())) {
                        a.collide(b);
                        b.collide(a);
                    }
                }
            }

            for (int dx = 0; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy <= 0) continue;
                    Cell neighborCell = new Cell(cell.x + dx, cell.y + dy);
                    Array<Entity> neighbors = grid.get(neighborCell);
                    if (neighbors == null) continue;

                    for (int i = 0; i < entitiesInCell.size; i++) {
                        Entity a = entitiesInCell.get(i);
                        for (int j = 0; j < neighbors.size; j++) {
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
        for (Array<Entity> cellEntities : grid.values()) {
            cellEntities.clear();
        }
    }

    public record Cell(int x, int y) {}
}
