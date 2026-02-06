package dev.creoii.chaos.client.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dev.creoii.chaos.OptionsManager;
import dev.creoii.chaos.client.ClientGame;
import dev.creoii.chaos.client.render.Renderer;
import dev.creoii.chaos.client.render.screen.InventoryScreen;
import dev.creoii.chaos.client.util.Inputtable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class InputManager extends InputAdapter {
    private final ClientGame game;
    private final List<Inputtable> inputs;
    private final Vector3 mousePos = new Vector3();
    private final Vector3 prevMousePos = new Vector3();
    private final Set<Integer> keysHeld;
    private final Set<TouchEntry> mouseKeysHeld;
    private boolean dragging;

    public InputManager(ClientGame game) {
        this.game = game;
        inputs = new ArrayList<>();
        keysHeld = new HashSet<>();
        mouseKeysHeld = new HashSet<>();
        dragging = false;
    }

    public ClientGame getGame() {
        return game;
    }

    public static boolean isShiftDown() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }

    public boolean isKeyHeld() {
        return !keysHeld.isEmpty();
    }

    public boolean isKeyHeld(int keycode) {
        return keysHeld.contains(keycode);
    }

    public Set<Integer> getKeysHeld() {
        return keysHeld;
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

    public Vector3 getPrevMousePos() {
        return prevMousePos;
    }

    public boolean isMouseMoving() {
        return mousePos.dst2(prevMousePos) > .01f;
    }

    public void update() {
        updateMouse(Gdx.input.getX(), Gdx.input.getY());

        for (int keycode : keysHeld) {
            keyHeld(keycode);
        }

        for (TouchEntry touchEntry : mouseKeysHeld) {
            touchHeld(touchEntry.pointer, touchEntry.button);
        }
    }

    private void forEach(Consumer<Inputtable> consumer) {
        for (int i = inputs.size() - 1; i >= 0; --i) {
            consumer.accept(inputs.get(i));
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        keysHeld.add(keycode);
        if (keycode == OptionsManager.DEBUG_KEY.intValue()) {
            game.setDebug(!game.isDebug());
            return true;
        } else if (keycode == OptionsManager.INVENTORY_KEY.intValue() && !game.getWorld().getChatManager().isActive()) {
            Renderer renderer = game.getRenderer();
            if (renderer.getCurrentScreen() == null) {
                renderer.setCurrentScreen(new InventoryScreen(game, new Vector2(1084, 400), game.getCharacter().slots));
            } else renderer.clearCurrentScreen();
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
        keysHeld.remove(keycode);
        forEach(inputtable -> inputtable.keyUp(this, keycode));
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0)
            return false;

        dragging = true;
        mouseKeysHeld.add(new TouchEntry(screenX, screenY, pointer, button));
        forEach(inputtable -> inputtable.touchDown(this, screenX, screenY, pointer, button));
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        dragging = true;
        forEach(inputtable -> inputtable.touchDragged(this, screenX, screenY, pointer));
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0)
            return false;

        dragging = false;
        mouseKeysHeld.removeIf(touchEntry -> touchEntry.button == button && touchEntry.pointer == pointer);
        forEach(inputtable -> inputtable.touchUp(this, screenX, screenY, pointer, button));
        return super.touchUp(screenX, screenY, pointer, button);
    }

    public void touchHeld(int pointer, int button) {
        forEach(inputtable -> inputtable.touchHeld(this, Gdx.input.getX(), Gdx.input.getY(), pointer, button));
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        dragging = false;
        mouseKeysHeld.removeIf(touchEntry -> touchEntry.button == button && touchEntry.pointer == pointer);
        forEach(inputtable -> inputtable.touchCancelled(this, screenX, screenY, pointer, button));
        return super.touchCancelled(screenX, screenY, pointer, button);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        game.getRenderer().updateZoom(amountY);
        return super.scrolled(amountX, amountY);
    }

    @Override
    public boolean keyTyped(char character) {
        forEach(inputtable -> inputtable.keyTyped(this, character));
        return super.keyTyped(character);
    }

    private void updateMouse(int screenX, int screenY) {
        prevMousePos.set(mousePos.x, mousePos.y, 0f);
        mousePos.set(screenX, screenY, 0f);
        game.getRenderer().getCamera().unproject(prevMousePos);
        game.getRenderer().getCamera().unproject(mousePos);
    }

    private record TouchEntry(int screenX, int screenY, int pointer, int button) { }
}
