package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityManager {
    private final Main main;
    private final Map<UUID, Entity> entities;

    public EntityManager(Main main) {
        this.main = main;
        this.entities = new HashMap<>();
    }

    public <E extends Entity, T extends EntityType<E>> E addEntity(T type, Vector2 pos) {
        return addEntity(type, pos, new HashMap<>());
    }

    public <E extends Entity, T extends EntityType<E>> E addEntity(T type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(main.getGame(), pos, customData);
        entities.put(spawned.getUuid(), spawned);
        main.getGame().getTickManager().addTickable(spawned); // add boolean value to not tick
        return spawned;
    }

    public Map<UUID, Entity> getEntities() {
        return entities;
    }

    public Entity getEntity(UUID uuid) {
        return entities.get(uuid);
    }

    public boolean removeEntity(Entity entity) {
        return removeEntity(entity.getUuid());
    }

    public boolean removeEntity(UUID uuid) {
        if (entities.containsKey(uuid)) {
            main.getGame().getTickManager().removeTickable(entities.get(uuid));
            entities.remove(uuid);
            return true;
        }
        return false;
    }
}
