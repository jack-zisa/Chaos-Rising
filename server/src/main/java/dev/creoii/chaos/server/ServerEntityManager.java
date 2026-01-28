package dev.creoii.chaos.server;

import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.EntityManager;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.EntityType;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.Tickable;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.Context;
import dev.creoii.chaos.util.event.SpawnEntityEvent;
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

    public ServerEntityManager(ServerWorld world) {
        super(world);
        removedEntities = new IntArrayList();
        moveEntries = new ObjectArrayList<>();
        world.getGame().getTickManager().addTickable(this);
    }

    @Override
    public <E extends Entity> E addEntity(E entity) {
        EntityType<?> type = entity.getType();
        getEntities(type.group()).put(entity.getId(), entity);

        getWorld().getGame().getServer().sendToAllTCP(new EntitySpawnS2C(entity.getId(), entity.getPos().x, entity.getPos().y, type.scale(), entity.getCustomPacketData()));

        SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getWorld(), entity.getId());

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

            SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getWorld(), entity.getId());

            spawnedEntities.add(new SpawnEntitiesS2C.Entry(entity.getId(), entity.getPos().x, entity.getPos().y, entity.getType().scale(), entity.getCustomPacketData()));
        }

        setSize(getSize() + size);

        size = spawnedEntities.size();
        for (int i = 0; i < size; i += 100) {
            getWorld().getGame().getServer().sendToAllTCP(new SpawnEntitiesS2C(spawnedEntities.subList(i, Math.min(i + 100, spawnedEntities.size()))));
        }
    }

    public <E extends Entity, T extends EntityType<E>> void addEntities(T type, VecProvider posProvider, Map<String, Object> customData, int count) {
        List<SpawnEntitiesS2C.Entry> spawnedEntities = new ArrayList<>();

        Context context = Context.rootOf(getWorld()).with(ComponentTypes.POS, Vector2.Zero.cpy());
        for (int i = 0; i < count; ++i) {
            Vector2 pos = posProvider.get(context);
            E spawned = type.create(getWorld(), getNextId(), pos, customData);
            getEntities(type.group()).put(spawned.getId(), spawned);

            SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getWorld(), spawned.getId());

            spawnedEntities.add(new SpawnEntitiesS2C.Entry(spawned.getId(), spawned.getPos().x, spawned.getPos().y, type.scale(), spawned.getCustomPacketData()));
        }

        setSize(getSize() + count);

        int size = spawnedEntities.size();
        for (int i = 0; i < size; i += SpawnEntitiesS2C.BATCH_SIZE) {
            getWorld().getGame().getServer().sendToAllTCP(new SpawnEntitiesS2C(spawnedEntities.subList(i, Math.min(i + SpawnEntitiesS2C.BATCH_SIZE, spawnedEntities.size()))));
        }
    }

    public <E extends Entity, T extends EntityType<E>> E addEntity(T type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(getWorld(), getNextId(), pos, customData);
        getEntities(type.group()).put(spawned.getId(), spawned);

        getWorld().getGame().getServer().sendToAllTCP(new EntitySpawnS2C(spawned.getId(), pos.x, pos.y, type.scale(), spawned.getCustomPacketData()));

        SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getWorld(), spawned.getId());

        setSize(getSize() + 1);
        return spawned;
    }

    public <E extends Entity, T extends EntityType<E>> E addCharacter(int connectionId, T type, Vector2 pos, Map<String, Object> customData) {
        E spawned = type.create(getWorld(), getNextId(), pos, customData);
        getEntities(type.group()).put(spawned.getId(), spawned);

        EntityCustomData data = spawned.getCustomPacketData();
        getWorld().getGame().getServer().sendToTCP(connectionId, new CharacterJoinS2C(spawned.getId(), pos.x, pos.y, type.scale(), data, getWorld().getSeed()));
        getWorld().getGame().getServer().sendToAllExceptTCP(connectionId, new EntitySpawnS2C(spawned.getId(), pos.x, pos.y, type.scale(), data));

        SpawnEntityEvent.EVENT.invoker().onSpawnEntity(getWorld(), spawned.getId());

        setSize(getSize() + 1);
        return spawned;
    }

    @Override
    public void tick(int gametime, float delta) {
        if (!removedEntities.isEmpty()) {
            for (int id : removedEntities) {
                for (Map.Entry<EntityGroup, Int2ObjectOpenHashMap<Entity>> entry : getAllEntities().entrySet()) {
                    Int2ObjectOpenHashMap<Entity> map = entry.getValue();
                    if (map.containsKey(id)) {
                        Entity entity = map.get(id);

                        if (entity instanceof LivingEntity living && living.hasParent()) {
                            if (getEntity(living.getParentId()) instanceof LivingEntity living1) {
                                living1.removeChild(id);
                            }
                        }

                        map.remove(id);
                        setSize(getSize() - 1);
                        free(id);
                    }
                }
            }

            getWorld().getGame().getServer().sendToAllTCP(new RemoveEntitiesS2C(removedEntities));

            removedEntities.clear();
        }

        if (getSize() <= 0 || gametime % 2 == 0)
            return;
        for (Int2ObjectOpenHashMap<Entity> map : getAllEntities().values()) {
            for (Entity entity : map.clone().values()) {
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
                getWorld().getGame().getServer().sendToAllUDP(new MoveEntitiesS2C(moveEntries.subList(i, Math.min(i + 50, moveEntries.size()))));
            }
        }
        moveEntries.clear();
    }

    @Override
    public boolean removeEntity(int id) {
        return removedEntities.add(id);
    }
}
