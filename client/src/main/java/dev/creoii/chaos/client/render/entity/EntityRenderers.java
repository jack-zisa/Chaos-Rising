package dev.creoii.chaos.client.render.entity;

import dev.creoii.chaos.client.render.entity.data.EntityRenderData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityRenderers {
    private static final Map<Class<? extends EntityRenderData>, Function<?, ?>> RENDERERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends EntityRenderData> EntityRenderer<T> getRenderer(T entity) {
        Function<T, EntityRenderer<T>> function = (Function<T, EntityRenderer<T>>) RENDERERS.get(entity.getClass());
        return function.apply(entity);
    }

    public static <T extends EntityRenderData> void register(Class<T> clazz, Function<T, EntityRenderer<T>> renderFunction) {
        RENDERERS.put(clazz, renderFunction);
    }
}
