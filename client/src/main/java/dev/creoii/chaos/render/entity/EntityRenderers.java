package dev.creoii.chaos.render.entity;

import dev.creoii.chaos.entity.ClientEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityRenderers {
    private static final Map<Class<? extends ClientEntity>, Function<ClientEntity, EntityRenderer<? extends ClientEntity>>> RENDERERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends ClientEntity> EntityRenderer<T> getRenderer(T entity) {
        return (EntityRenderer<T>) RENDERERS.get(entity.getClass()).apply(entity);
    }

    public static void register(Class<? extends ClientEntity> clazz, Function<ClientEntity, EntityRenderer<? extends ClientEntity>> renderFunction) {
        RENDERERS.put(clazz, renderFunction);
    }
}
