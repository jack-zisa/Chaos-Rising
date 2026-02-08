package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.entity.data.BulletEntityRenderData;

import javax.annotation.Nullable;

public class BulletEntityRenderer extends SimpleEntityRenderer<BulletEntityRenderData> {
    public BulletEntityRenderer(BulletEntityRenderData entity) {
        super(entity);
    }

    @Override
    public void render(BulletEntityRenderData entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        Sprite sprite = entity.sprite;
        if (batch != null) {
            sprite.setPosition(entity.renderX, entity.renderY);

            sprite.setOriginCenter();

            entity.angle += entity.rotationSpeed * delta;
            sprite.setRotation(entity.angle);

            batch.disableBlending();
            sprite.draw(batch);
            batch.enableBlending();
        }

        if (debug && shapeRenderer != null) {
            EntityRenderer.renderDebugCollisionBox(shapeRenderer, entity, sprite);
        }
    }
}
