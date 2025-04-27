package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.ServerEntity;
import dev.creoii.chaos.entity.EntityType;
import dev.creoii.chaos.entity.ServerLivingEntity;
import dev.creoii.chaos.entity.character.ServerCharacterEntity;
import dev.creoii.chaos.network.packet.s2c.EntitySpawnS2C;
import dev.creoii.chaos.network.packet.s2c.EntityStateS2C;
import dev.creoii.chaos.network.packet.s2c.LivingEntityStateS2C;
import dev.creoii.chaos.util.Tickable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityManager implements Tickable {
    private final ServerGame game;
    private final Map<UUID, ServerEntity> entities;
    private final Map<UUID, ServerCharacterEntity> characters;

    public EntityManager(ServerGame game) {
        this.game = game;
        entities = new HashMap<>();
        characters = new HashMap<>();

        game.getTickManager().addTickable(this);
    }

    @Override
    public void tick(int gametime, float delta) {
        entities.forEach((uuid, entity) -> {
            game.getServer().sendToAllTCP(new EntityStateS2C(uuid, entity.getPos().x, entity.getPos().y));

            if (entity instanceof ServerLivingEntity living) {
                game.getServer().sendToAllTCP(new LivingEntityStateS2C(uuid, living.getStats(), living.getMaxStats()));
            }
        });
    }

    public <E extends ServerEntity, T extends EntityType<E>> E addEntity(T type, Vector2 pos) {
        return addEntity(UUID.randomUUID(), type, pos, new HashMap<>());
    }

    public <E extends ServerEntity, T extends EntityType<E>> E addEntity(UUID uuid, T type, Vector2 pos) {
        return addEntity(uuid, type, pos, new HashMap<>());
    }

    public <E extends ServerEntity, T extends EntityType<E>> E addEntity(UUID uuid, T type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(game, uuid, pos, customData);
        entities.put(spawned.getUuid(), spawned);

        if (spawned instanceof ServerCharacterEntity character)
            characters.put(spawned.getUuid(), character);

        game.getTickManager().addTickable(spawned); // add boolean value to not tick
        game.getServer().sendToAllTCP(new EntitySpawnS2C(uuid, spawned.getGroup(), spawned.getType().textureId(), spawned.getPos().x, spawned.getPos().y, spawned.getType().scale()));
        return spawned;
    }

    public Map<UUID, ServerEntity> getEntities() {
        return entities;
    }

    public Map<UUID, ServerCharacterEntity> getCharacters() {
        return characters;
    }

    public ServerEntity getEntity(UUID uuid) {
        return entities.get(uuid);
    }

    public ServerCharacterEntity getCharacter(UUID uuid) {
        return characters.get(uuid);
    }

    public boolean removeEntity(UUID uuid) {
        if (entities.containsKey(uuid)) {
            game.getTickManager().removeTickable(entities.get(uuid));
            characters.remove(uuid);
            entities.remove(uuid);
            return true;
        }
        return false;
    }
}
