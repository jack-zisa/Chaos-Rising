package dev.creoii.chaos.server;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.EntityManager;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.EntityType;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Tickable;

import java.util.*;

public class ServerEntityManager extends EntityManager<Entity> implements Tickable {
    private final List<Integer> killedEntities;

    public ServerEntityManager(ServerGame game) {
        super(game);
        killedEntities = new ArrayList<>();
        game.getTickManager().addTickable(this);
    }

    public <E extends Entity, T extends EntityType<E>> E addEntity(T type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(getGame(), getNextId(), pos, customData);
        getEntities(type.group()).put(spawned.getId(), spawned);

        if (getGame() instanceof ServerGame serverGame) {
            serverGame.getTickManager().addTickable(spawned);
            serverGame.getServer().sendToAllTCP(new EntitySpawnS2C(spawned.getId(), pos.x, pos.y, spawned.getCustomPacketData()));
            serverGame.getServer().sendToAllTCP(new EntityDisplayS2C(spawned.getId(), type.id(), type.scale()));
        }

        setSize(getSize() + 1);
        return spawned;
    }

    @Override
    public void tick(int gametime, float delta) {
        if (!killedEntities.isEmpty()) {
            getGame().getServer().sendToAllTCP(new RemoveEntitiesS2C(killedEntities));
            killedEntities.clear();
        }

        if (getSize() <= 0 || gametime % 2 == 0)
            return;
        List<MoveEntitiesS2C.Entry> entries = new ArrayList<>();
        getAllEntities().values().forEach(uuidEntityMap -> uuidEntityMap.values().forEach(entity -> {
            entity.tick(gametime, delta);

            if (!entity.canMove() || entity.getType().group() == EntityGroup.CHARACTER)
                return;

            Vector2 velocity = entity.getVelocity();
            if (velocity.x != 0f || velocity.y != 0f) {
                entries.add(new MoveEntitiesS2C.Entry(entity.getId(), entity.getPos().x, entity.getPos().y, velocity.x, velocity.y));
            }
        }));
        if (!entries.isEmpty()) {
            for (int i = 0; i < entries.size(); i += 50) {
                getGame().getServer().sendToAllTCP(new MoveEntitiesS2C(entries.subList(i, Math.min(i + 50, entries.size()))));
            }
        }
    }

    @Override
    public boolean removeEntity(int id) {
        for (Map.Entry<EntityGroup, Map<Integer, Entity>> entry : getAllEntities().entrySet()) {
            Map<Integer, Entity> map = entry.getValue();
            if (map.containsKey(id)) {
                ((ServerGame) getGame()).getTickManager().removeTickable(map.get(id));
                map.remove(id);
                setSize(getSize() - 1);
                free(id);
                killedEntities.add(id);
                return true;
            }
        }
        return false;
    }
}
