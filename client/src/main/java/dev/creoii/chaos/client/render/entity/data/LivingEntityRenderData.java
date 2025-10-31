package dev.creoii.chaos.client.render.entity.data;

import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

public class LivingEntityRenderData extends EntityRenderData {
    public StatContainer statContainer;
    public StatContainer maxStatContainer;
    public boolean facingRight;

    public LivingEntityRenderData(int id, EntityGroup group, float x, float y, float xv, float yv, String textureId, float scale, StatContainer statContainer, StatContainer maxStatContainer) {
        super(id, group, x, y, xv, yv, textureId, scale);
        this.statContainer = statContainer;
        this.maxStatContainer = maxStatContainer;
    }

    public boolean canMove() {
        return statContainer.speed().value() > 0;
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);

        if (xv > .01f) {
            facingRight = true;
        } else if (xv < -.01f) {
            facingRight = false;
        }
    }
}
