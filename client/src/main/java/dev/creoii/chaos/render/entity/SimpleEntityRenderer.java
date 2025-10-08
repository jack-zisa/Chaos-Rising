package dev.creoii.chaos.render.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.render.entity.data.EntityRenderData;
import dev.creoii.chaos.render.entity.data.LivingEntityRenderData;

import javax.annotation.Nullable;

public class SimpleEntityRenderer<T extends EntityRenderData> extends EntityRenderer<T> {
    public SimpleEntityRenderer(T entity) {
        super(entity);
    }

    @Override
    public void init(EntityRenderManager manager) {

    }

    public void render(T entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        Sprite sprite = EntityRenderManager.getSprite(renderer.getGame(), entity);
        if (batch != null) {
            sprite.setPosition(entity.x, entity.y);
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
            shapeRenderer.end();
        }
    }
}
