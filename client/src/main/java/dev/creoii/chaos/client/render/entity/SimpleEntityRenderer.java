package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.entity.data.EntityRenderData;
import dev.creoii.chaos.client.render.entity.data.LivingEntityRenderData;

import javax.annotation.Nullable;

public class SimpleEntityRenderer<T extends EntityRenderData> extends EntityRenderer<T> {
    public SimpleEntityRenderer(T entity) {
        super(entity);
    }

    @Override
    public void init(EntityRenderManager manager) {

    }

    public void render(T entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        Sprite sprite = EntityRenderManager.getSprite(renderer.getGame(), entity);
        if (batch != null) {
            float predictedX = entity.x + entity.xv * delta;
            float predictedY = entity.y + entity.yv * delta;

            float alpha = 1f - (float) Math.pow(.001f, delta);
            entity.renderX += (predictedX - entity.renderX) * alpha;
            entity.renderY += (predictedY - entity.renderY) * alpha;

            sprite.setPosition(entity.renderX, entity.renderY);
            sprite.draw(batch);

            if (entity instanceof LivingEntityRenderData livingEntity) {
                float scale = livingEntity.scale;
                float baseX = entity.x + (scale / 2f) - 16f;
                float baseY = entity.y + scale;

                /*for (int i = 0; i < livingEntity.getStatusEffects().size(); ++i) {
                    StatusEffect statusEffect = livingEntity.getStatusEffects().get(i);
                    Sprite effectSprite = new Sprite(renderer.getGame().getTextureManager().getTexture("effect", statusEffect.getType().id()));

                    float x = baseX + ((i % 4f) * 8f);
                    float y = baseY + ((i / 4f) * 8f) + 4f;

                    effectSprite.setPosition(x, y);
                    effectSprite.draw(batch);
                }*/
            }
        }

        if (debug && shapeRenderer != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(entity.x, entity.y, sprite.getWidth(), sprite.getHeight());
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(entity.renderX, entity.renderY, sprite.getWidth(), sprite.getHeight());
            shapeRenderer.end();
        }
    }
}
