package dev.creoii.chaos.client.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import dev.creoii.chaos.client.render.Renderer;

import javax.annotation.Nullable;

public interface Renderable {
    default void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, float delta, boolean debug) {

    }

    default void setupStage(Renderer renderer, Stage stage, Skin skin, boolean debug) {

    }

    default void renderStage(Renderer renderer, Stage stage, boolean debug) {

    }
}
