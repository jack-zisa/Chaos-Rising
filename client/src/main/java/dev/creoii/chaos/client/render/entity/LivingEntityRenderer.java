package dev.creoii.chaos.client.render.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.entity.data.LivingEntityRenderData;

import javax.annotation.Nullable;

public class LivingEntityRenderer<T extends LivingEntityRenderData> extends SimpleEntityRenderer<T> {
    public LivingEntityRenderer(T entity) {
        super(entity);
    }

    public void render(T entity, Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {
        super.render(entity, renderer, batch, shapeRenderer, font, delta, debug);
    }
}
