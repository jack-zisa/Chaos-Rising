package dev.creoii.chaos.util;

import dev.creoii.chaos.InputManager;

public interface Inputtable {
    default boolean keyDown(InputManager manager, int keycode) {
        return false;
    }

    default void keyHeld(InputManager manager, int keycode) {

    }

    default boolean keyUp(InputManager manager, int keycode) {
        return false;
    }

    default boolean keyTyped(InputManager manager, char character) {
        return false;
    }

    default boolean touchDown(InputManager manager, int screenX, int screenY, int pointer, int button) {
        return false;
    }

    default boolean touchUp(InputManager manager, int screenX, int screenY, int pointer, int button) {
        return false;
    }

    default boolean touchCancelled(InputManager manager, int screenX, int screenY, int pointer, int button) {
        return false;
    }

    default boolean touchDragged(InputManager manager, int screenX, int screenY, int pointer) {
        return false;
    }

    default boolean mouseMoved(InputManager manager, int screenX, int screenY) {
        return false;
    }

    default boolean scrolled(InputManager manager, float amountX, float amountY) {
        return false;
    }
}
