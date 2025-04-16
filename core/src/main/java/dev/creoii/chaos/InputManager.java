package dev.creoii.chaos;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.creoii.chaos.render.screen.InventoryScreen;
import dev.creoii.chaos.util.Inputtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InputManager extends InputAdapter {
    private final Main main;
    private final Map<String, Integer> keymap;
    private final List<Inputtable> inputs;
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
        inputs = new ArrayList<>();
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

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public int getKeycode(String key) {
        return keymap.getOrDefault(key, -1);
    }

    public List<Inputtable> getInputs() {
        return inputs;
    }

    public void addInput(Inputtable inputtable) {
        inputs.add(inputtable);
    }

    public void removeInput(Inputtable inputtable) {
        inputs.remove(inputtable);
    }

    public Vector3 getMousePos() {
        return mousePos;
    }

    protected void update(int gametime) {
        if (isKeyHeld()) {
            keyHeld(keyHeld);
        }
    }

    private void forEach(Consumer<Inputtable> consumer) {
        for (int i = inputs.size() - 1; i >= 0; --i) {
            consumer.accept(inputs.get(i));
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
                main.getRenderer().setCurrentScreen(new InventoryScreen(getMain(), new Vector2(Main.WINDOW_WIDTH - 196, 400), main.getGame().getActiveCharacter().getInventory()));
            else
                main.getRenderer().clearCurrentScreen();
            return true;
        }

        forEach(inputtable -> inputtable.keyDown(this, keycode));

        return false;
    }

    public void keyHeld(int keycode) {
        forEach(inputtable -> inputtable.keyHeld(this, keycode));
    }

    @Override
    public boolean keyUp(int keycode) {
        keyHeld = -1;
        forEach(inputtable -> inputtable.keyUp(this, keycode));
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0)
            return false;
        main.getRenderer().getCamera().unproject(mousePos.set(screenX, screenY, 0));
        dragging = true;
        forEach(inputtable -> inputtable.touchDown(this, screenX, screenY, pointer, button));
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        dragging = true;
        main.getRenderer().getCamera().unproject(mousePos.set(screenX, screenY, 0));
        forEach(inputtable -> inputtable.touchDragged(this, screenX, screenY, pointer));
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0)
            return false;
        main.getRenderer().getCamera().unproject(mousePos.set(screenX, screenY, 0));
        dragging = false;
        forEach(inputtable -> inputtable.touchUp(this, screenX, screenY, pointer, button));
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        dragging = false;
        forEach(inputtable -> inputtable.touchCancelled(this, screenX, screenY, pointer, button));
        return super.touchCancelled(screenX, screenY, pointer, button);
    }
}
