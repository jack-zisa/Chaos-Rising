package dev.creoii.chaos.render.entity;

import dev.creoii.chaos.render.data.BulletEntityRenderData;

public class BulletEntityRenderer extends SimpleEntityRenderer<BulletEntityRenderData> {
    public BulletEntityRenderer(BulletEntityRenderData entity) {
        super(entity);
    }

    @Override
    public void init(EntityRenderManager manager) {
        //float angle = (float) Math.atan2(yDir, xDir) * (180f / (float) Math.PI) % 360f;
        //getSprite().setOriginCenter();
        //getSprite().setRotation(angle - angleOffset.get(Provider.Context.of(this, game.getGametime())));
    }
}
