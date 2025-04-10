package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import dev.creoii.chaos.entity.Entity;

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
        return addEntity(entity, pos, new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(T entity, Vector2 pos, Map<String, Object> customData) {
        UUID uuid = UUID.randomUUID();
        T spawned = (T) entity.spawn(main.getGame(), uuid, pos, customData);
        entities.put(uuid, spawned);
        main.getGame().getTickManager().addTickable(spawned); // add boolean value to not tick
        return spawned;
    }

    public Entity getEntity(UUID uuid) {
        return entities.get(uuid);
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
            if (entity != null && entity.getGame() != null) {
                activeEntities.put(entry.key, entity);
            }
        }
        return activeEntities;
    }
}
