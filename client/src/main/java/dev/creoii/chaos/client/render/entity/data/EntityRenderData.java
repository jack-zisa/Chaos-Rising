package dev.creoii.chaos.client.render.entity.data;

import com.badlogic.gdx.graphics.g2d.Sprite;
import dev.creoii.chaos.util.EntityGroup;

public class EntityRenderData {
    public Sprite sprite;
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
        this.xv = xv;
        this.yv = yv;
        this.textureId = textureId;
        this.scale = scale;
        renderX = x;
        renderY = y;
        sprite = null;
    }

    public boolean canMove() {
        return false;
    }

    public void tick(float delta) {
        if (!canMove())
            return;

        float alpha = Math.min(1f, delta * 15f);

        renderX += (x - renderX) * alpha;
        renderY += (y - renderY) * alpha;
    }
}
