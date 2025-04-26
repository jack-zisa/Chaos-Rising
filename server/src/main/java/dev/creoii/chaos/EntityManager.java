package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.ServerEntity;
import dev.creoii.chaos.entity.EntityType;
import dev.creoii.chaos.entity.ServerLivingEntity;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.network.packet.s2c.EntitySpawnS2C;
import dev.creoii.chaos.network.packet.s2c.EntityStateS2C;
import dev.creoii.chaos.network.packet.s2c.LivingEntityStateS2C;
import dev.creoii.chaos.util.Tickable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityManager implements Tickable {
    private final ServerMain main;
    private final Map<UUID, ServerEntity> entities;
    private final Map<UUID, CharacterEntity> characters;

    public EntityManager(ServerMain main) {
        this.main = main;
        entities = new HashMap<>();
        characters = new HashMap<>();

        main.getGame().getTickManager().addTickable(this);
    }

    @Override
    public void tick(int gametime, float delta) {
        entities.forEach((uuid, entity) -> {
            main.getServer().sendToAllTCP(new EntityStateS2C(uuid, entity.getPos().x, entity.getPos().y));

            if (entity instanceof ServerLivingEntity living) {
                main.getServer().sendToAllTCP(new LivingEntityStateS2C(uuid, living.getStats(), living.getMaxStats()));
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
        E spawned = type.create(main.getGame(), uuid, pos, customData);
        entities.put(spawned.getUuid(), spawned);

        if (spawned instanceof CharacterEntity character)
            characters.put(spawned.getUuid(), character);

        main.getGame().getTickManager().addTickable(spawned); // add boolean value to not tick
        main.getServer().sendToAllTCP(new EntitySpawnS2C(uuid, spawned.getGroup(), spawned.getType().textureId(), spawned.getPos().x, spawned.getPos().y, spawned.getType().scale()));
        return spawned;
    }

    public Map<UUID, ServerEntity> getEntities() {
        return entities;
    }

    public Map<UUID, CharacterEntity> getCharacters() {
        return characters;
    }

    public ServerEntity getEntity(UUID uuid) {
        return entities.get(uuid);
    }

    public CharacterEntity getCharacter(UUID uuid) {
        return characters.get(uuid);
    }

    public boolean removeEntity(UUID uuid) {
        if (entities.containsKey(uuid)) {
            main.getGame().getTickManager().removeTickable(entities.get(uuid));
            characters.remove(uuid);
            entities.remove(uuid);
            return true;
        }
        return false;
    }
}
