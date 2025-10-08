package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.EntityType;
import dev.creoii.chaos.network.packet.s2c.EntityDisplayS2C;
import dev.creoii.chaos.network.packet.s2c.EntityMoveS2C;
import dev.creoii.chaos.network.packet.s2c.EntitySpawnS2C;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Tickable;

import java.util.Map;
import java.util.UUID;

public class ServerEntityManager extends EntityManager<Entity> implements Tickable {
    public ServerEntityManager(ServerGame game) {
        super(game);
        game.getTickManager().addTickable(this);
    }

    public <E extends Entity, T extends EntityType<E>> E addEntity(UUID uuid, T type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(getGame(), uuid, pos, customData);
        getEntities(type.group()).put(spawned.getUuid(), spawned);

        if (getGame() instanceof ServerGame serverGame) {
            serverGame.getTickManager().addTickable(spawned);
            serverGame.getServer().sendToAllTCP(new EntitySpawnS2C(spawned.getUuid(), spawned.getType().group(), pos.x, pos.y));
            serverGame.getServer().sendToAllTCP(new EntityDisplayS2C(spawned.getUuid(), type.id(), type.scale()));
        }
        return spawned;
    }

    @Override
    public void tick(int gametime, float delta) {
        getAllEntities().values().forEach(uuidEntityMap -> uuidEntityMap.values().forEach(entity -> {
            entity.tick(gametime, delta);

            if (entity.getType().group() == EntityGroup.CHARACTER)
                return;

            Vector2 velocity = entity.getVelocity();
            if (velocity.x != 0f || velocity.y != 0f) {
                ((ServerGame) getGame()).getServer().sendToAllTCP(new EntityMoveS2C(entity.getUuid(), entity.getPos().x, entity.getPos().y, entity.getPos().x - entity.getPrevPos().x, entity.getPos().y - entity.getPrevPos().y));
            }
        }));
    }

    @Override
    public boolean removeEntity(UUID uuid) {
        for (Map.Entry<EntityGroup, Map<UUID, Entity>> entry : getAllEntities().entrySet()) {
            if (entry.getValue().containsKey(uuid)) {
                ((ServerGame) getGame()).getTickManager().removeTickable(entry.getValue().get(uuid));
                entry.getValue().remove(uuid);
                return true;
            }
        }
        return false;
    }
}
