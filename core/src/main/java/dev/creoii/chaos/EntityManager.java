package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.util.EntityGroup;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManager<T> {
    private final Game game;
    private final Map<EntityGroup, Map<UUID, T>> entities;
    private int size;

    public EntityManager(Game game) {
        this.game = game;
        entities = new HashMap<>();
        for (EntityGroup group : EntityGroup.values()) {
            entities.put(group, new ConcurrentHashMap<>());
        }
        size = 0;
    }

    public Game getGame() {
        return game;
    }

    public int getSize() {
        return size;
    }

    public <E extends Entity, ET extends EntityType<E>> E addEntity(ET type, Vector2 pos) {
        return addEntity(UUID.randomUUID(), type, pos, new HashMap<>());
    }

    public <E extends Entity, ET extends EntityType<E>> E addEntity(UUID uuid, ET type, Vector2 pos) {
        return addEntity(uuid, type, pos, new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity, ET extends EntityType<E>> E addEntity(UUID uuid, ET type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(game, uuid, pos, customData);
        entities.get(type.group()).put(spawned.getUuid(), (T) spawned);
        ++size;
        return spawned;
    }

    public Map<EntityGroup, Map<UUID, T>> getAllEntities() {
        return entities;
    }

    public Map<UUID, T> getEntities(EntityGroup group) {
        return entities.get(group);
    }

    @Nullable
    public T getEntity(EntityGroup group, UUID uuid) {
        return entities.get(group).get(uuid);
    }

    @Nullable
    public T getEntity(UUID uuid) {
        for (Map.Entry<EntityGroup, Map<UUID, T>> entry : getAllEntities().entrySet()) {
            if (uuid != null && entry.getValue().containsKey(uuid)) {
                return entry.getValue().get(uuid);
            }
        }
        return null;
    }

    public boolean removeEntity(UUID uuid) {
        if (entities.containsKey(uuid)) {
            entities.remove(uuid);
            --size;
            return true;
        }
        return false;
    }
}
