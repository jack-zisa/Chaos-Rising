package dev.creoii.chaos.render.entity;

import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.network.packet.util.EntityGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityRenderers {
    private static final Map<EntityGroup, Function<Entity, EntityRenderer<? extends Entity>>> RENDERERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Entity> EntityRenderer<T> getRenderer(T entity) {
        return (EntityRenderer<T>) RENDERERS.get(entity.getGroup()).apply(entity);
    }

    public static void register(EntityGroup group, Function<Entity, EntityRenderer<? extends Entity>> renderFunction) {
        RENDERERS.put(group, renderFunction);
    }
}
