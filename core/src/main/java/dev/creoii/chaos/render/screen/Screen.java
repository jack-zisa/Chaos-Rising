package dev.creoii.chaos.render.screen;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.util.Renderable;

import javax.annotation.Nullable;

public abstract class Screen implements Renderable {
    private final String title;
    private final Vector2 pos;

    public Screen(String title, Vector2 pos) {
        this.title = title;
        this.pos = pos;
    }

    public String getTitle() {
        return title;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (batch == null)
            return;
        font.draw(batch, title, pos.x, pos.y);
    }
}
