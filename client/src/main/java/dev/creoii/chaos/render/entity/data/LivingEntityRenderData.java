package dev.creoii.chaos.render.entity.data;

import dev.creoii.chaos.util.EntityGroup;

import java.util.UUID;

public class LivingEntityRenderData extends EntityRenderData {
    public LivingEntityRenderData(UUID uuid, EntityGroup group, float x, float y, float xv, float yv, String textureId, float scale) {
        super(uuid, group, x, y, xv, yv, textureId, scale);
    }
}
