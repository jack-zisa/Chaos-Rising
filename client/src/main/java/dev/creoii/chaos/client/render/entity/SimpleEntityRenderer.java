package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.entity.data.EntityRenderData;
import dev.creoii.chaos.client.render.entity.data.LivingEntityRenderData;
import dev.creoii.chaos.client.texture.TextureManager;
import dev.creoii.chaos.effect.StatusEffect;

import javax.annotation.Nullable;

public class SimpleEntityRenderer<T extends EntityRenderData> extends EntityRenderer<T> {
    public SimpleEntityRenderer(T entity) {
        super(entity);
    }

    @Override
    public void init(EntityRenderManager manager, T entity) {
        entity.sprite = EntityRenderManager.getSprite((ClientGame) manager.getWorld().getGame(), entity);
    }

    @Override
    public void render(T entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        Sprite sprite = entity.sprite;
        if (batch != null) {
            if (entity instanceof LivingEntityRenderData livingEntityRenderData) {
                sprite.setFlip(!livingEntityRenderData.facingRight, false);

                renderStatusEffects(renderer.getGame(), batch, livingEntityRenderData);
            }

            float pixelW = 1f / (8f * EntityRenderManager.BORDER_SIZE_MOD);
            float pixelH = 1f / (8f * EntityRenderManager.BORDER_SIZE_MOD);

            TextureRegion region = new TextureRegion(sprite.getTexture(), entity.renderX - pixelW, entity.renderY - pixelH, sprite.getRegionWidth() + pixelW * 2f, sprite.getRegionHeight() + pixelH * 2f);
            batch.draw(region, entity.renderX - pixelW, entity.renderY - pixelH, sprite.getRegionWidth() + pixelW * 2f, sprite.getRegionHeight() + pixelH * 2f);
        }

        if (debug && shapeRenderer != null) {
            EntityRenderer.renderDebugCollisionBox(shapeRenderer, entity, sprite);
        }
    }

    protected static void renderStatusEffects(ClientGame game, SpriteBatch batch, LivingEntityRenderData livingEntityRenderData) {
        float baseX = livingEntityRenderData.x + (livingEntityRenderData.scale / 2f) - 16f;
        float baseY = livingEntityRenderData.y + livingEntityRenderData.scale;

        for (int i = 0; i < livingEntityRenderData.statusEffects.size(); ++i) {
            StatusEffect.Instance instance = livingEntityRenderData.statusEffects.get(i);
            Sprite effectSprite = new Sprite(game.getAssetManager().getTextureManager().getTexture(TextureManager.Atlas.EFFECT, instance.getEffect().id()));

            float x = baseX + ((i % 4f) * 8f);
            float y = baseY + ((i / 4f) * 8f) + 4f;

            effectSprite.setPosition(x, y);
            effectSprite.draw(batch);
        }
    }
}
