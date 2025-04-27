package dev.creoii.chaos.render.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.render.Renderer;

import javax.annotation.Nullable;

public class SimpleEntityRenderer<T extends Entity> extends EntityRenderer<T> {
    public SimpleEntityRenderer(T entity) {
        super(entity);
    }

    public void render(T entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        Sprite sprite = EntityRenderManager.getSprite(renderer.getGame(), entity);
        if (batch != null) {
            sprite.setPosition(entity.getPos().x, entity.getPos().y);
            sprite.draw(batch);

            if (entity instanceof LivingEntity livingEntity) {
                float scale = 1f;
                float baseX = entity.getPos().x + (scale / 2f) - 16f;
                float baseY = entity.getPos().y + scale;

                for (int i = 0; i < livingEntity.getStatusEffects().size(); ++i) {
                    StatusEffect statusEffect = livingEntity.getStatusEffects().get(i);
                    Sprite effectSprite = new Sprite(renderer.getGame().getTextureManager().getTexture("effect", statusEffect.getType().id()));

                    float x = baseX + ((i % 4f) * 8f);
                    float y = baseY + ((i / 4f) * 8f) + 4f;

                    effectSprite.setPosition(x, y);
                    effectSprite.draw(batch);
                }
            }
        }

        if (debug && shapeRenderer != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(entity.getPos().x, entity.getPos().y, sprite.getWidth(), sprite.getHeight());
            shapeRenderer.end();
        }
    }
}
