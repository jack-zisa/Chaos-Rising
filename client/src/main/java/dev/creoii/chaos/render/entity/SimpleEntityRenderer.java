package dev.creoii.chaos.render.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.entity.ClientEntity;
import dev.creoii.chaos.render.Renderer;

import javax.annotation.Nullable;

public class SimpleEntityRenderer<T extends ClientEntity> extends EntityRenderer<T> {
    public SimpleEntityRenderer(T entity) {
        super(entity);
    }

    public void render(T entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (batch != null && entity.getSprite() != null) {
            entity.getSprite().setPosition(entity.getPos().x, entity.getPos().y);
            entity.getSprite().draw(batch);

            /*if (entity instanceof LivingEntity livingEntity) {
                float baseX = entity.getPos().x + (entity.getScale() / 2f) - 16f;
                float baseY = entity.getPos().y + entity.getScale();

                for (int i = 0; i < livingEntity.getStatusEffects().size(); ++i) {
                    StatusEffect statusEffect = livingEntity.getStatusEffects().get(i);
                    Sprite sprite = StatusEffects.EFFECT_TEXTURES.get(statusEffect.id());

                    float x = baseX + ((i % 4f) * 8f);
                    float y = baseY + ((i / 4f) * 8f) + 4f;

                    sprite.setPosition(x, y);
                    sprite.draw(batch);
                }
            }*/
        }

        if (debug && shapeRenderer != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(entity.getPos().x, entity.getPos().y, entity.getSprite().getWidth(), entity.getSprite().getHeight());
            shapeRenderer.end();
        }
    }
}
