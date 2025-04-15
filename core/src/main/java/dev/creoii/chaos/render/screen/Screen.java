package dev.creoii.chaos.render.screen;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.util.Inputtable;
import dev.creoii.chaos.util.Renderable;

import javax.annotation.Nullable;

public abstract class Screen implements Renderable, Inputtable {
    private final String title;
    private final Vector2 pos;
    private final float titleOffsetY;

    public Screen(String title, Vector2 pos, float titleOffsetY) {
        this.title = title;
        this.pos = pos;
        this.titleOffsetY = titleOffsetY;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void open(Main main) {
        main.getGame().getInputManager().addInput(this);
    }

    public void close(Main main) {
        main.getGame().getInputManager().removeInput(this);
    }

    public void render(Renderer renderer, @Nullable SpriteBatch batch, @Nullable ShapeRenderer shapeRenderer, BitmapFont font, boolean debug) {
        if (batch == null)
            return;
        font.draw(batch, title, pos.x, pos.y + titleOffsetY);
    }

    @Override
    public boolean keyDown(InputManager manager, int keycode) {
        if (keycode == manager.getKeycode("back")) {
            manager.getMain().getRenderer().clearCurrentScreen();
        }
        return Inputtable.super.keyDown(manager, keycode);
    }
}
