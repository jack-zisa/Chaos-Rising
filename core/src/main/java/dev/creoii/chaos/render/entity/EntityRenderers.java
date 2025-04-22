package dev.creoii.chaos.render.entity;

import dev.creoii.chaos.entity.Entity;

import java.util.*;
import java.util.function.Function;

public class EntityRenderers {
    private static final Map<Class<?>, Function<Entity, EntityRenderer<? extends Entity>>> RENDERERS = new HashMap<>();
    protected static final Set<Class<?>> INVISIBLES = new HashSet<>();

    @SuppressWarnings("unchecked")
    public static <T extends Entity> EntityRenderer<T> getRenderer(T entity) {
        return (EntityRenderer<T>) RENDERERS.get(entity.getClass()).apply(entity);
    }

    public static void register(Class<?> clazz, Function<Entity, EntityRenderer<? extends Entity>> renderFunction) {
        RENDERERS.put(clazz, renderFunction);
    }

    public static void registerInvisible(Class<?> clazz) {
        INVISIBLES.add(clazz);
    }
}
