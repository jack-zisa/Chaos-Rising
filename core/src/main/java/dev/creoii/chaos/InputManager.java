package dev.creoii.chaos;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.HashMap;
import java.util.Map;

public class InputManager extends InputAdapter {
    private final Main main;
    private final Map<String, Integer> keymap;

    public InputManager(Main main) {
        this.main = main;
        this.keymap = new HashMap<>();
        keymap.put("up", Input.Keys.W);
        keymap.put("down", Input.Keys.S);
        keymap.put("left", Input.Keys.A);
        keymap.put("right", Input.Keys.D);
        keymap.put("debug", Input.Keys.F3);
        keymap.put("command", Input.Keys.SLASH);
    }

    public int getKeycode(String key) {
        return keymap.getOrDefault(key, -1);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == getKeycode("debug")) {
            main.setDebug(!main.getDebug());
            return true;
        }
        return false;
    }
}
