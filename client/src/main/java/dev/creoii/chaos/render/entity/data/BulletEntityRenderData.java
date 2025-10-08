package dev.creoii.chaos.render.entity.data;


import dev.creoii.chaos.util.EntityGroup;

import java.util.UUID;

public class BulletEntityRenderData extends EntityRenderData {
    public float xd;
    public float yd;

    public BulletEntityRenderData(UUID uuid, float x, float y, float xv, float yv, String textureId, float scale, float xd, float yd) {
        super(uuid, EntityGroup.BULLET, x, y, xv, yv, textureId, scale);
        this.xd = xd;
        this.yd = yd;
    }
}
