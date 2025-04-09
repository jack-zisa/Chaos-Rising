package dev.creoii.chaos.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import dev.creoii.chaos.entity.Entity;

import javax.annotation.Nullable;

public class SimpleEntityRenderer<T extends Entity> extends EntityRenderer<T> {
    public SimpleEntityRenderer(T entity) {
        super(entity);
    }

    public void render(T entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (batch != null) {
            entity.getSprite().setPosition(entity.getPos().x, entity.getPos().y);
            entity.getSprite().draw(batch);
        }

        if (debug && shapeRenderer != null) {
            shapeRenderer.setColor(Color.GREEN);
            Rectangle collider = entity.getColliderRect();
            shapeRenderer.rect(collider.x, collider.y, collider.width, collider.height);
        }
    }
}
