package dev.creoii.chaos.server;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.EntityManager;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.EntityType;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Tickable;
import dev.creoii.chaos.util.event.SpawnEntityEvent;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.*;

public class ServerEntityManager extends EntityManager<Entity> implements Tickable {
    private final IntList removedEntities;
    private final ObjectList<MoveEntitiesS2C.Entry> moveEntries;

    public ServerEntityManager(ServerGame game) {
        super(game);
        removedEntities = new IntArrayList();
        moveEntries = new ObjectArrayList<>();
        game.getTickManager().addTickable(this);
    }

    @Override
    public <E extends Entity> E addEntity(E entity) {
        EntityType<?> type = entity.getType();
        getEntities(type.group()).put(entity.getId(), entity);

        ((ServerGame) getGame()).getTickManager().addTickable(entity);
        getGame().getServer().sendToAllTCP(new EntitySpawnS2C(entity.getId(), entity.getPos().x, entity.getPos().y, entity.getType().scale(), entity.getCustomPacketData()));

        SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getGame(), entity.getId());

        setSize(getSize() + 1);
        return entity;
    }

    @Override
    public <E extends Entity> void addEntities(List<E> entities) {
        List<SpawnEntitiesS2C.Entry> spawnedEntities = new ArrayList<>();

        int size = entities.size();
        for (int i = 0; i < size; ++i) {
            E entity = entities.get(i);

            getEntities(entity.getType().group()).put(entity.getId(), entity);
            ((ServerGame) getGame()).getTickManager().addTickable(entity);

            SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getGame(), entity.getId());

            spawnedEntities.add(new SpawnEntitiesS2C.Entry(entity.getId(), entity.getPos().x, entity.getPos().y, entity.getType().scale(), entity.getCustomPacketData()));
        }

        setSize(getSize() + size);

        size = spawnedEntities.size();
        for (int i = 0; i < size; i += 50) {
            getGame().getServer().sendToAllTCP(new SpawnEntitiesS2C(spawnedEntities.subList(i, Math.min(i + 50, spawnedEntities.size()))));
        }
    }

    public <E extends Entity, T extends EntityType<E>> void addEntities(T type, VecProvider posProvider, Map<String, Object> customData, int count) {
        List<SpawnEntitiesS2C.Entry> spawnedEntities = new ArrayList<>();

        for (int i = 0; i < count; ++i) {
            Vector2 pos = posProvider.get(new Provider.Context(getGame(), null, getGame().getGametime(), Vector2.Zero, getGame().getRandom()));
            E spawned = type.create(getGame(), getNextId(), pos, customData);
            getEntities(type.group()).put(spawned.getId(), spawned);

            ((ServerGame) getGame()).getTickManager().addTickable(spawned);

            SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getGame(), spawned.getId());

            spawnedEntities.add(new SpawnEntitiesS2C.Entry(spawned.getId(), spawned.getPos().x, spawned.getPos().y, type.scale(), spawned.getCustomPacketData()));
        }

        setSize(getSize() + count);

        int size = spawnedEntities.size();
        for (int i = 0; i < size; i += 50) {
            getGame().getServer().sendToAllTCP(new SpawnEntitiesS2C(spawnedEntities.subList(i, Math.min(i + 50, spawnedEntities.size()))));
        }
    }

    public <E extends Entity, T extends EntityType<E>> E addEntity(T type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(getGame(), getNextId(), pos, customData);
        getEntities(type.group()).put(spawned.getId(), spawned);

        ((ServerGame) getGame()).getTickManager().addTickable(spawned);
        getGame().getServer().sendToAllTCP(new EntitySpawnS2C(spawned.getId(), pos.x, pos.y, type.scale(), spawned.getCustomPacketData()));

        SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getGame(), spawned.getId());

        setSize(getSize() + 1);
        return spawned;
    }

    public <E extends Entity, T extends EntityType<E>> E addCharacter(int connectionId, T type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(getGame(), getNextId(), pos, customData);
        getEntities(type.group()).put(spawned.getId(), spawned);

        ((ServerGame) getGame()).getTickManager().addTickable(spawned);

        EntityCustomData data = spawned.getCustomPacketData();
        getGame().getServer().sendToTCP(connectionId, new CharacterJoinS2C(spawned.getId(), pos.x, pos.y, type.scale(), data));
        getGame().getServer().sendToAllExceptTCP(connectionId, new EntitySpawnS2C(spawned.getId(), pos.x, pos.y, type.scale(), data));

        SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getGame(), spawned.getId());

        setSize(getSize() + 1);
        return spawned;
    }

    @Override
    public void tick(int gametime, float delta) {
        if (!removedEntities.isEmpty()) {
            getGame().getServer().sendToAllTCP(new RemoveEntitiesS2C(removedEntities));
            removedEntities.clear();
        }

        if (getSize() <= 0 || gametime % 2 == 0)
            return;
        for (Int2ObjectOpenHashMap<Entity> map : getAllEntities().values()) {
            for (Entity entity : map.values()) {
                entity.tick(gametime, delta);

                if (!entity.canMove() || entity.getType().group() == EntityGroup.CHARACTER)
                    continue;

                Vector2 velocity = entity.getVelocity();
                if (velocity.x != 0f || velocity.y != 0f) {
                    moveEntries.add(new MoveEntitiesS2C.Entry(entity.getId(), entity.getPos().x, entity.getPos().y, velocity.x, velocity.y));
                }
            }
        }
        if (!moveEntries.isEmpty()) {
            int size = moveEntries.size();
            for (int i = 0; i < size; i += 50) {
                getGame().getServer().sendToAllTCP(new MoveEntitiesS2C(moveEntries.subList(i, Math.min(i + 50, moveEntries.size()))));
            }
        }
        moveEntries.clear();
    }

    @Override
    public boolean removeEntity(int id) {
        for (Map.Entry<EntityGroup, Int2ObjectOpenHashMap<Entity>> entry : getAllEntities().entrySet()) {
            Int2ObjectOpenHashMap<Entity> map = entry.getValue();
            if (map.containsKey(id)) {
                ((ServerGame) getGame()).getTickManager().removeTickable(map.get(id));
                map.remove(id);
                setSize(getSize() - 1);
                free(id);
                removedEntities.add(id);
                return true;
            }
        }
        return false;
    }
}
