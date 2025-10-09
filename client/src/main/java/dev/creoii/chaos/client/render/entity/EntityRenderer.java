package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.data.EntityRenderData;

import javax.annotation.Nullable;

public abstract class EntityRenderer<T extends EntityRenderData> {
    private final T entity;

    public EntityRenderer(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public abstract void render(T entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug);

    public abstract void init(EntityRenderManager manager);
}
