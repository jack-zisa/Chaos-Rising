package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.entity.data.BulletEntityRenderData;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.Context;

import javax.annotation.Nullable;

public class BulletEntityRenderer extends SimpleEntityRenderer<BulletEntityRenderData> {
    private Context context;

    public BulletEntityRenderer(BulletEntityRenderData entity) {
        super(entity);
    }

    @Override
    public void render(BulletEntityRenderData entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        Sprite sprite = entity.sprite;
        if (batch != null) {
            if (context == null) {
                context = Context.rootOf(renderer.getGame().getWorld());
            }

            sprite.setPosition(entity.renderX, entity.renderY);
            context.set(ComponentTypes.POS, new Vector2(entity.renderX, entity.renderY));

            sprite.setOriginCenter();
            sprite.setRotation((MathUtils.atan2(entity.yv, entity.xv) * MathUtils.radiansToDegrees) + entity.angleOffset.get(context));

            sprite.draw(batch);
        }

        if (debug && shapeRenderer != null) {
            EntityRenderer.renderDebugCollisionBox(shapeRenderer, entity, sprite);
        }
    }
}
