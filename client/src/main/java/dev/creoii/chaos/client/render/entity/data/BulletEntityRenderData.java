package dev.creoii.chaos.client.render.entity.data;

import dev.creoii.chaos.util.EntityGroup;

public class BulletEntityRenderData extends EntityRenderData {
    public float xd;
    public float yd;
    public float angle;

    public BulletEntityRenderData(int id, float x, float y, float xv, float yv, String textureId, float scale, float xd, float yd, float angle) {
        super(id, EntityGroup.BULLET, x, y, xv, yv, textureId, scale);
        this.xd = xd;
        this.yd = yd;
        this.angle = angle;
    }

    @Override
    public boolean canMove() {
        return true;
    }
}
