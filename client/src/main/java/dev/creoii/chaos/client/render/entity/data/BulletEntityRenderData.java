package dev.creoii.chaos.client.render.entity.data;


import dev.creoii.chaos.util.EntityGroup;

public class BulletEntityRenderData extends EntityRenderData {
    public float xd;
    public float yd;

    public BulletEntityRenderData(int id, float x, float y, float xv, float yv, String textureId, float scale, float xd, float yd) {
        super(id, EntityGroup.BULLET, x, y, xv, yv, textureId, scale);
        this.xd = xd;
        this.yd = yd;
    }
}
