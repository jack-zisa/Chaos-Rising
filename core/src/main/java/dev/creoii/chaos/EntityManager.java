package dev.creoii.chaos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import dev.creoii.chaos.entity.*;
import dev.creoii.chaos.network.s2c.EntityRemoveS2C;
import dev.creoii.chaos.util.EntityGroup;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManager<T> {
    private static int NEXT_ID = 0;
    private final Game game;
    private final Deque<Integer> freeIds;
    private final Map<EntityGroup, Map<Integer, T>> entities;
    private final Map<EntityGroup, Pool<Entity>> pools;
    private int size;

    public EntityManager(Game game) {
        this.game = game;
        this.freeIds = new ArrayDeque<>();
        entities = new HashMap<>();

        pools = new EnumMap<>(EntityGroup.class);
        for (EntityGroup group : EntityGroup.values()) {
            entities.put(group, new ConcurrentHashMap<>());

            pools.put(group, new Pool<>(group.getPoolSize()) {
                @Override
                protected Entity newObject() {
                    return null;
                }
            });
        }

        size = 0;
    }

    public int getNextId() {
        if (!freeIds.isEmpty()) {
            return freeIds.pop();
        }
        return NEXT_ID++;
    }

    public Game getGame() {
        return game;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public <E extends Entity, ET extends EntityType<E>> E addEntity(ET type, Vector2 pos) {
        return addEntity(type, pos, new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity, ET extends EntityType<E>> E addEntity(ET type, Vector2 pos, Map<String, Object> customData) {
        Pool<Entity> pool = pools.get(type.group());
        E spawned = (E) pool.obtain();

        if (spawned == null) {
            spawned = type.create(game, getNextId(), pos, customData);
        } else {
            spawned.reinit(getNextId(), pos, customData);
        }

        entities.get(type.group()).put(spawned.getId(), (T) spawned);
        ++size;
        return spawned;
    }

    public Map<EntityGroup, Map<Integer, T>> getAllEntities() {
        return entities;
    }

    public Map<Integer, T> getEntities(EntityGroup group) {
        return entities.get(group);
    }

    @Nullable
    public T getEntity(EntityGroup group, int id) {
        return entities.get(group).get(id);
    }

    @Nullable
    public T getEntity(int id) {
        for (Map.Entry<EntityGroup, Map<Integer, T>> entry : getAllEntities().entrySet()) {
            if (id != -1 && entry.getValue().containsKey(id)) {
                return entry.getValue().get(id);
            }
        }
        return null;
    }

    public boolean removeEntity(int id) {
        for (Map.Entry<EntityGroup, Map<Integer, T>> entry : getAllEntities().entrySet()) {
            if (id != -1 && entry.getValue().containsKey(id)) {
                Entity entity = (Entity) entry.getValue().remove(id);
                if (entity != null) {
                    if (!game.isClient())
                        game.getServer().sendToAllTCP(new EntityRemoveS2C(id));
                    --size;
                    free(id);
                    pools.get(entity.getType().group()).free(entity);
                }
                return true;
            }
        }
        return false;
    }

    public void free(int id) {
        freeIds.push(id);
    }
}
