package dev.creoii.chaos.render.entity;

import dev.creoii.chaos.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityRenderers {
    private static final Map<Class<?>, Function<Entity, EntityRenderer<? extends Entity>>> RENDERERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Entity> EntityRenderer<T> getRenderer(T entity) {
        return (EntityRenderer<T>) RENDERERS.get(entity.getClass()).apply(entity);
    }

    public static void register(Class<?> clazz, Function<Entity, EntityRenderer<? extends Entity>> renderFunction) {
        RENDERERS.put(clazz, renderFunction);
    }
}
