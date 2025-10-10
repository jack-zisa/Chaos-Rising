package dev.creoii.chaos.client.render.entity.data;

import dev.creoii.chaos.util.EntityGroup;

public class EntityRenderData {
    public final int id;
    public final EntityGroup group;
    public float x;
    public float y;
    public float xv;
    public float yv;
    public String textureId;
    public float scale;
    public float renderX;
    public float renderY;

    public EntityRenderData(int id, EntityGroup group, float x, float y, float xv, float yv, String textureId, float scale) {
        this.id = id;
        this.group = group;
        this.x = x;
        this.y = y;
        renderX = x;
        renderY = y;
        this.xv = xv;
        this.yv = yv;
        this.textureId = textureId;
        this.scale = scale;
    }

    public boolean canMove() {
        return false;
    }
}
