package dev.creoii.chaos.client.render.entity.data;

import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public class BulletEntityRenderData extends EntityRenderData {
    public float xd;
    public float yd;
    public NumberProvider angleOffset;

    public BulletEntityRenderData(int id, float x, float y, float xv, float yv, String textureId, float scale, float xd, float yd, NumberProvider angleOffset) {
        super(id, EntityGroup.BULLET, x, y, xv, yv, textureId, scale);
        this.xd = xd;
        this.yd = yd;
        this.angleOffset = angleOffset;
    }

    @Override
    public boolean canMove() {
        return true;
    }
}
