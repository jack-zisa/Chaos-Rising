package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.entity.data.EntityRenderData;

import javax.annotation.Nullable;

public abstract class EntityRenderer<T extends EntityRenderData> {
    private final T entity;

    public EntityRenderer(T entity) {
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public abstract void render(T entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug);

    public abstract void init(EntityRenderManager manager, T entity);

    protected static void renderDebugCollisionBox(ShapeRenderer shapeRenderer, EntityRenderData entity, Sprite sprite) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(entity.x, entity.y, sprite.getWidth(), sprite.getHeight());
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(entity.renderX, entity.renderY, sprite.getWidth(), sprite.getHeight());
        shapeRenderer.end();
    }
}
