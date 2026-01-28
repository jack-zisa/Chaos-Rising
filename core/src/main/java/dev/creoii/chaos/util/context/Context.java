package dev.creoii.chaos.util.context;

import dev.creoii.chaos.Game;
import dev.creoii.chaos.World;
import dev.creoii.chaos.entity.Entity;

import java.util.Arrays;

public final class Context implements ContextProvider {
    private final Game game;
    private final Object[] components;
    private final ContextProvider parent;

    public Context(Game game, ContextProvider parent) {
        this.game = game;
        this.parent = parent;
        this.components = new Object[ComponentType.count()];
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public ContextProvider getParent() {
        return parent;
    }

    @Override
    public Context getContext() {
        return this;
    }

    public static Context rootOf(Entity entity) {
        Context context = new Context(entity.getWorld().getGame(), null);
        return context
            .with(ComponentTypes.WORLD, entity.getWorld())
            .with(ComponentTypes.ENTITY, entity)
            .with(ComponentTypes.TIME, entity.getWorld().getGame().getGametime())
            .with(ComponentTypes.POS, entity.getPos())
            .with(ComponentTypes.RANDOM, entity.getWorld().getRandom());
    }

    public static Context rootOf(World world) {
        Context context = new Context(world.getGame(), null);
        return context
            .with(ComponentTypes.WORLD, world)
            .with(ComponentTypes.TIME, world.getGame().getGametime())
            .with(ComponentTypes.RANDOM, world.getRandom());
    }

    public static Context root(Game game) {
        return new Context(game, null);
    }

    public Context child() {
        return new Context(game, this);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ComponentType<T> type) {
        Object value = components[type.id];
        if (value != null) {
            return (T) value;
        }
        return parent != null ? parent.get(type) : null;
    }

    public <T> void set(ComponentType<T> type, T value) {
        components[type.id] = value;
    }

    public <T> Context with(ComponentType<T> type, T value) {
        components[type.id] = value;
        return this;
    }

    public <T> boolean has(ComponentType<T> type) {
        return components[type.id] != null || (parent != null && parent.has(type));
    }

    public void clearLocal() {
        Arrays.fill(components, null);
    }
}
