package dev.creoii.chaos;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.creoii.chaos.render.screen.InventoryScreen;
import dev.creoii.chaos.util.Inputtable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InputManager extends InputAdapter {
    private final Main main;
    private final List<Inputtable> inputs;
    private final Vector3 mousePos = new Vector3();
    private int keyHeld;
    private boolean dragging;

    public InputManager(Main main) {
        this.main = main;
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
        if (keycode == main.getGame().getOptionsManager().DEBUG_KEY.intValue()) {
            main.setDebug(!main.getDebug());
            return true;
        } else if (keycode == main.getGame().getOptionsManager().INVENTORY_KEY.intValue()) {
            if (main.getRenderer().getCurrentScreen() == null) {
                main.getRenderer().setCurrentScreen(new InventoryScreen(main, new Vector2(1084, 400), main.getGame().getActiveCharacter().getInventory()));
            } else
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
