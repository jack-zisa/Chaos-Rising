package dev.creoii.chaos.render.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.Main;
import dev.creoii.chaos.render.Renderer;
import dev.creoii.chaos.render.screen.widget.Widget;
import dev.creoii.chaos.util.Inputtable;
import dev.creoii.chaos.util.Renderable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class Screen implements Renderable, Inputtable {
    private static final NinePatch SCREEN_BACKGROUND = new NinePatch(new Texture("textures/ui/screen_background.png"), 4, 4, 4, 4);
    private final Map<String, Widget> widgets;
    private final String title;
    private final Vector2 pos;
    private final float titleOffsetY;

    public Screen(String title, Vector2 pos, float titleOffsetY) {
        this.title = title;
        this.pos = pos;
        this.titleOffsetY = titleOffsetY;
        widgets = new HashMap<>();
    }

    public Vector2 getPos() {
        return pos;
    }

    public void addWidget(String key, Widget widget) {
        widgets.put(key, widget);
    }

    public void removeWidget(String key) {
        widgets.remove(key);
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
        //SCREEN_BACKGROUND.draw(batch, pos.x, pos.y, 200, 200);
        font.draw(batch, title, pos.x, pos.y + titleOffsetY);

        widgets.forEach((key, widget) -> widget.render(renderer, batch, shapeRenderer, font, debug));
    }

    @Override
    public boolean keyDown(InputManager manager, int keycode) {
        if (keycode == manager.getKeycode("back")) {
            manager.getMain().getRenderer().clearCurrentScreen();
        }
        widgets.forEach((key, widget) -> widget.keyDown(manager, keycode));
        return Inputtable.super.keyDown(manager, keycode);
    }

    @Override
    public void keyHeld(InputManager manager, int keycode) {
        widgets.forEach((key, widget) -> widget.keyHeld(manager, keycode));
        Inputtable.super.keyHeld(manager, keycode);
    }

    @Override
    public boolean keyUp(InputManager manager, int keycode) {
        widgets.forEach((key, widget) -> widget.keyUp(manager, keycode));
        return Inputtable.super.keyUp(manager, keycode);
    }

    @Override
    public boolean keyTyped(InputManager manager, char character) {
        widgets.forEach((key, widget) -> widget.keyTyped(manager, character));
        return Inputtable.super.keyTyped(manager, character);
    }

    @Override
    public boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        widgets.forEach((key, widget) -> widget.touchDown(manager, screenX, screenY, pointer, button));
        return Inputtable.super.touchDown(manager, screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(InputManager manager, int screenX, int screenY, int pointer, int button) {
        widgets.forEach((key, widget) -> widget.touchUp(manager, screenX, screenY, pointer, button));
        return Inputtable.super.touchUp(manager, screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchCancelled(InputManager manager, int screenX, int screenY, int pointer, int button) {
        widgets.forEach((key, widget) -> widget.touchCancelled(manager, screenX, screenY, pointer, button));
        return Inputtable.super.touchCancelled(manager, screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(InputManager manager, int screenX, int screenY, int pointer) {
        widgets.forEach((key, widget) -> widget.touchDragged(manager, screenX, screenY, pointer));
        return Inputtable.super.touchDragged(manager, screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(InputManager manager, int screenX, int screenY) {
        widgets.forEach((key, widget) -> widget.mouseMoved(manager, screenX, screenY));
        return Inputtable.super.mouseMoved(manager, screenX, screenY);
    }

    @Override
    public boolean scrolled(InputManager manager, float amountX, float amountY) {
        widgets.forEach((key, widget) -> widget.scrolled(manager, amountX, amountY));
        return Inputtable.super.scrolled(manager, amountX, amountY);
    }
}
