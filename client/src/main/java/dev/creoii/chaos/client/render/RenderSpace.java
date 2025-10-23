package dev.creoii.chaos.client.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.creoii.chaos.util.function.QuadConsumer;

public enum RenderSpace {
    WORLD((_, spriteBatch, shapeRenderer, camera) -> {
        shapeRenderer.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);
    }),
    SCREEN((viewport, spriteBatch, shapeRenderer, _) -> {
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
    }),
    STAGE((_, _, _, _) -> {});

    private final QuadConsumer<Viewport, SpriteBatch, ShapeRenderer, Camera> setupConsumer;

    RenderSpace(QuadConsumer<Viewport, SpriteBatch, ShapeRenderer, Camera> setupConsumer) {
        this.setupConsumer = setupConsumer;
    }

    void setup(Viewport viewport, SpriteBatch batch, ShapeRenderer shapeRenderer, Camera camera) {
        setupConsumer.accept(viewport, batch, shapeRenderer, camera);
    }
}
