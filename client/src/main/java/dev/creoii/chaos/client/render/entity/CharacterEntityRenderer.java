package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.entity.data.CharacterEntityRenderData;

import javax.annotation.Nullable;

public class CharacterEntityRenderer extends EntityRenderer<CharacterEntityRenderData> {
    public CharacterEntityRenderer(CharacterEntityRenderData entity) {
        super(entity);
    }

    @Override
    public void init(EntityRenderManager manager, CharacterEntityRenderData entity) {
        entity.sprite = EntityRenderManager.getSprite((ClientGame) manager.getGame(), entity);
    }

    @Override
    public void render(CharacterEntityRenderData entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        Sprite sprite = entity.sprite;
        if (batch != null) {
            sprite.setPosition(entity.renderX, entity.renderY);
            sprite.draw(batch);
            sprite.setFlip(!entity.facingRight, false);
        }

        if (debug && shapeRenderer != null) {
            EntityRenderer.renderDebugCollisionBox(shapeRenderer, entity, sprite);
        }
    }
}
