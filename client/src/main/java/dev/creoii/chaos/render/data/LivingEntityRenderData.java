package dev.creoii.chaos.render.data;

import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.UUID;

public class LivingEntityRenderData extends EntityRenderData {
    public StatContainer statContainer;
    public StatContainer maxStatContainer;

    public LivingEntityRenderData(UUID uuid, EntityGroup group, float x, float y, float xv, float yv, String textureId, float scale, StatContainer statContainer, StatContainer maxStatContainer) {
        super(uuid, group, x, y, xv, yv, textureId, scale);
        this.statContainer = statContainer;
        this.maxStatContainer = maxStatContainer;
    }

    public boolean canMove() {
        return statContainer.speed().value() > 0;
    }
}
