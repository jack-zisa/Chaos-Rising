package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityManager {
    private final Main main;
    private final ObjectMap<UUID, Entity> entities;

    public EntityManager(Main main) {
        this.main = main;
        this.entities = new ObjectMap<>();
    }

    public <T extends Entity> T addEntity(T entity, Vector2 pos) {
        UUID uuid = UUID.randomUUID();
        T spawned = (T) entity.spawn(main.getGame(), uuid, pos);
        entities.put(uuid, spawned);
        main.getGame().getTickManager().addTickable(spawned); // add boolean value to not tick
        return spawned;
    }

    public boolean removeEntity(Entity entity) {
        if (entities.containsKey(entity.getUuid())) {
            main.getGame().getTickManager().removeTickable(entity);
            entities.remove(entity.getUuid());
            return true;
        }
        return false;
    }

    public Map<UUID, Entity> getActiveEntities() {
        Map<UUID, Entity> activeEntities = new HashMap<>();
        for (ObjectMap.Entry<UUID, Entity> entry : entities) {
            Entity entity = entry.value;
            if (entity != null && entity.isActive()) {
                activeEntities.put(entry.key, entity);
            }
        }
        return activeEntities;
    }

    public Vector2 getCell(Vector2 pos) {
        return new Vector2((int) (pos.x / 64f), (int) (pos.y / 64f));
    }

    public void checkCollisions() {
        Map<Vector2, Array<LivingEntity>> grid = new HashMap<>();

        for (Entity entity : getActiveEntities().values()) {
            if (entity instanceof LivingEntity livingEntity) {
                Vector2 cell = getCell(entity.getPos());
                grid.computeIfAbsent(cell, k -> new Array<>()).add(livingEntity);
            }
        }

        for (Map.Entry<Vector2, Array<LivingEntity>> entry : grid.entrySet()) {
            Vector2 cell = entry.getKey();
            Array<LivingEntity> entitiesInCell = entry.getValue();

            for (int i = 0; i < entitiesInCell.size; i++) {
                LivingEntity a = entitiesInCell.get(i);
                for (int j = i + 1; j < entitiesInCell.size; j++) {
                    LivingEntity b = entitiesInCell.get(j);
                    if (a.getColliderRect().overlaps(b.getColliderRect())) {
                        a.collide(b);
                        b.collide(a);
                    }
                }
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    Vector2 neighborCell = new Vector2(cell.x + dx, cell.y + dy);
                    Array<LivingEntity> neighbors = grid.get(neighborCell);
                    if (neighbors == null) continue;

                    for (int i = 0; i < entitiesInCell.size; i++) {
                        LivingEntity a = entitiesInCell.get(i);
                        for (int j = 0; j < neighbors.size; j++) {
                            LivingEntity b = neighbors.get(j);
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
}
