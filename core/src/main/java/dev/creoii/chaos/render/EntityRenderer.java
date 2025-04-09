package dev.creoii.chaos.render;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.entity.Entity;

import javax.annotation.Nullable;

public abstract class EntityRenderer<T extends Entity> {
    private final T entity;

    public EntityRenderer(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public abstract void render(T entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug);
}
