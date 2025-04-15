package dev.creoii.chaos;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.creoii.chaos.render.screen.InventoryScreen;

import java.util.HashMap;
import java.util.Map;

public class InputManager extends InputAdapter {
    private final Main main;
    private final Map<String, Integer> keymap;
    private final Vector3 mousePos = new Vector3();
    private int keyHeld;
    private boolean dragging;

    public InputManager(Main main) {
        this.main = main;
        this.keymap = new HashMap<>();
        keymap.put("up", Input.Keys.W);
        keymap.put("down", Input.Keys.S);
        keymap.put("left", Input.Keys.A);
        keymap.put("right", Input.Keys.D);
        keymap.put("debug", Input.Keys.F3);
        keymap.put("command", Input.Keys.SLASH);
        keymap.put("inventory", Input.Keys.E);
        keymap.put("back", Input.Keys.ESCAPE);
        keyHeld = -1;
        dragging = false;
    }

    public Main getMain() {
        return main;
    }

    public boolean isKeyHeld() {
        return keyHeld >= 0;
    }

    public int getKeyHeld() {
        return keyHeld;
    }

    public boolean isDragging() {
        return dragging;
    }

    public int getKeycode(String key) {
        return keymap.getOrDefault(key, -1);
    }

    public Vector3 getMousePos() {
        return mousePos;
    }

    protected void update(int gametime) {
        if (isKeyHeld()) {
            keyHeld(keyHeld);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        keyHeld = keycode;
        if (keycode == getKeycode("debug")) {
            main.setDebug(!main.getDebug());
            return true;
        } else if (keycode == getKeycode("inventory")) {
            if (main.getRenderer().getCurrentScreen() == null)
                main.getRenderer().setCurrentScreen(new InventoryScreen(new Vector2(Main.WINDOW_WIDTH - 196, 400), main.getGame().getActiveCharacter().getInventory()));
            else main.getRenderer().clearCurrentScreen();
        }

        if (main.getRenderer().getCurrentScreen() != null) {
            main.getRenderer().getCurrentScreen().control(this, keycode);
        }

        return false;
    }

    public boolean keyHeld(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyHeld = -1;
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0)
            return false;
        main.getRenderer().getCamera().unproject(mousePos.set(screenX, screenY, 0));
        dragging = true;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!dragging)
            return false;
        main.getRenderer().getCamera().unproject(mousePos.set(screenX, screenY, 0));
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0)
            return false;
        main.getRenderer().getCamera().unproject(mousePos.set(screenX, screenY, 0));
        dragging = false;
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        dragging = false;
        return super.touchCancelled(screenX, screenY, pointer, button);
    }
}
