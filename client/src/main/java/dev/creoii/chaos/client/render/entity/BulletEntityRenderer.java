package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.entity.data.BulletEntityRenderData;
import dev.creoii.chaos.util.provider.Provider;

import javax.annotation.Nullable;

public class BulletEntityRenderer extends SimpleEntityRenderer<BulletEntityRenderData> {
    public BulletEntityRenderer(BulletEntityRenderData entity) {
        super(entity);
    }

    @Override
    public void render(BulletEntityRenderData entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        Sprite sprite = entity.sprite;
        if (batch != null) {
            float predictedX = entity.x + entity.xv * delta;
            float predictedY = entity.y + entity.yv * delta;

            float alpha = 1f - (float) Math.pow(.001f, delta);
            entity.renderX += (predictedX - entity.renderX) * alpha;
            entity.renderY += (predictedY - entity.renderY) * alpha;

            sprite.setPosition(entity.renderX, entity.renderY);

            sprite.setOriginCenter();
            sprite.setRotation((MathUtils.atan2(entity.yv, entity.xv) * MathUtils.radiansToDegrees) + entity.angleOffset.get(new Provider.Context(renderer.getGame(), null, 0, new Vector2(entity.x, entity.y), renderer.getGame().getRandom())));

            sprite.draw(batch);
        }

        if (debug && shapeRenderer != null) {
            EntityRenderer.renderDebugCollisionBox(shapeRenderer, entity, sprite);
        }
    }
}
