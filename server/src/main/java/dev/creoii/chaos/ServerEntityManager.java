package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.EntityType;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.network.packet.s2c.EntitySpawnS2C;
import dev.creoii.chaos.network.packet.s2c.EntityStateS2C;
import dev.creoii.chaos.network.packet.s2c.LivingEntityStateS2C;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Tickable;

import java.util.Map;
import java.util.UUID;

public class ServerEntityManager extends EntityManager implements Tickable {
    public ServerEntityManager(ServerGame game) {
        super(game);
        game.getTickManager().addTickable(this);
    }

    @Override
    public <E extends Entity, T extends EntityType<E>> E addEntity(UUID uuid, T type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(getGame(), uuid, pos, customData);
        getEntities().put(spawned.getUuid(), spawned);

        if (spawned instanceof CharacterEntity character)
            getCharacters().put(spawned.getUuid(), character);

        if (getGame() instanceof ServerGame serverGame) {
            serverGame.getTickManager().addTickable(spawned);
            serverGame.getServer().sendToAllTCP(new EntitySpawnS2C(spawned.getType().group(), spawned.getUuid(), spawned.getType().id(), spawned.getPos()));
        }
        return spawned;
    }

    @Override
    public void tick(int gametime, float delta) {
        if (getGame() instanceof ServerGame serverGame) {
            getEntities().forEach((uuid, entity) -> {
                if (entity.getType().group() == EntityGroup.CHARACTER)
                    return;

                serverGame.getServer().sendToAllTCP(new EntityStateS2C(uuid, entity.getPos().x, entity.getPos().y));

                if (entity instanceof LivingEntity living) {
                    serverGame.getServer().sendToAllTCP(new LivingEntityStateS2C(uuid, living.getStats(), living.getMaxStats()));
                }
            });
        }
    }

    @Override
    public boolean removeEntity(UUID uuid) {
        if (getEntities().containsKey(uuid)) {
            if (getGame() instanceof ServerGame serverGame) {
                serverGame.getTickManager().removeTickable(getEntities().get(uuid));
            }
            getCharacters().remove(uuid);
            getEntities().remove(uuid);
            return true;
        }
        return false;
    }
}
