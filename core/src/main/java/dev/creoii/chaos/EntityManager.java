package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityManager {
    private final Game game;
    private final Map<UUID, Entity> entities;
    private final Map<UUID, CharacterEntity> characters;

    public EntityManager(Game game) {
        this.game = game;
        entities = new HashMap<>();
        characters = new HashMap<>();
    }

    public <E extends Entity, T extends EntityType<E>> E addEntity(T type, Vector2 pos) {
        return addEntity(UUID.randomUUID(), type, pos, new HashMap<>());
    }

    public <E extends Entity, T extends EntityType<E>> E addEntity(UUID uuid, T type, Vector2 pos) {
        return addEntity(uuid, type, pos, new HashMap<>());
    }

    public <E extends Entity, T extends EntityType<E>> E addEntity(UUID uuid, T type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(game, uuid, pos, customData);
        entities.put(spawned.getUuid(), spawned);

        if (spawned instanceof CharacterEntity character)
            characters.put(spawned.getUuid(), character);

        return spawned;
    }

    public Game getGame() {
        return game;
    }

    public Map<UUID, Entity> getEntities() {
        return entities;
    }

    public Map<UUID, CharacterEntity> getCharacters() {
        return characters;
    }

    public Entity getEntity(UUID uuid) {
        return entities.get(uuid);
    }

    public CharacterEntity getCharacter(UUID uuid) {
        return characters.get(uuid);
    }

    public boolean removeEntity(UUID uuid) {
        if (entities.containsKey(uuid)) {
            characters.remove(uuid);
            entities.remove(uuid);
            return true;
        }
        return false;
    }
}
