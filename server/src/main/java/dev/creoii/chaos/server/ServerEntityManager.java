package dev.creoii.chaos.server;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.EntityManager;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.EntityType;
import dev.creoii.chaos.network.s2c.EntityDisplayS2C;
import dev.creoii.chaos.network.s2c.MoveEntitiesS2C;
import dev.creoii.chaos.network.s2c.EntityRemoveS2C;
import dev.creoii.chaos.network.s2c.EntitySpawnS2C;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Tickable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServerEntityManager extends EntityManager<Entity> implements Tickable {
    public ServerEntityManager(ServerGame game) {
        super(game);
        game.getTickManager().addTickable(this);
    }

    @Override
    public int getTickRate() {
        return 3;
    }

    public <E extends Entity, T extends EntityType<E>> E addEntity(UUID uuid, T type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(getGame(), uuid, pos, customData);
        getEntities(type.group()).put(spawned.getUuid(), spawned);

        if (getGame() instanceof ServerGame serverGame) {
            serverGame.getTickManager().addTickable(spawned);
            serverGame.getServer().sendToAllTCP(new EntitySpawnS2C(spawned.getUuid(), pos.x, pos.y, spawned.getCustomPacketData()));
            serverGame.getServer().sendToAllTCP(new EntityDisplayS2C(spawned.getUuid(), type.id(), type.scale()));
        }

        setSize(getSize() + 1);
        return spawned;
    }

    @Override
    public void tick(int gametime, float delta) {
        if (getSize() <= 0)
            return;
        List<MoveEntitiesS2C.Entry> entries = new ArrayList<>();
        getAllEntities().values().forEach(uuidEntityMap -> uuidEntityMap.values().forEach(entity -> {
            entity.tick(gametime, delta);

            if (!entity.canMove())
                return;

            Vector2 velocity = entity.getVelocity();
            if (velocity.x != 0f || velocity.y != 0f) {
                entries.add(new MoveEntitiesS2C.Entry(entity.getUuid(), entity.getPos().x, entity.getPos().y, velocity.x, velocity.y));
            }
        }));
        if (!entries.isEmpty()) {
            for (int i = 0; i < entries.size(); i += 100) {
                getGame().getServer().sendToAllTCP(new MoveEntitiesS2C(entries.subList(i, Math.min(i + 100, entries.size()))));
            }
        }
    }

    @Override
    public boolean removeEntity(UUID uuid) {
        for (Map.Entry<EntityGroup, Map<UUID, Entity>> entry : getAllEntities().entrySet()) {
            Map<UUID, Entity> map = entry.getValue();
            if (map.containsKey(uuid)) {
                ((ServerGame) getGame()).getTickManager().removeTickable(map.get(uuid));
                map.remove(uuid);
                getGame().getServer().sendToAllTCP(new EntityRemoveS2C(uuid));
                setSize(getSize() - 1);
                return true;
            }
        }
        return false;
    }
}
