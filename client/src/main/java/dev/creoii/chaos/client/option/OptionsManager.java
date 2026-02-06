package dev.creoii.chaos.client.option;

import com.badlogic.gdx.Input;

public final class OptionsManager {
    public static final IntOption UP_KEY = new IntOption("key_forwards", Input.Keys.W);
    public static final IntOption DOWN_KEY = new IntOption("key_backwards", Input.Keys.S);
    public static final IntOption LEFT_KEY = new IntOption("key_left", Input.Keys.A);
    public static final IntOption RIGHT_KEY = new IntOption("key_right", Input.Keys.D);
    public static final IntOption DEBUG_KEY = new IntOption("key_debug", Input.Keys.F3);
    public static final IntOption COMMAND_KEY = new IntOption("key_command", Input.Keys.SLASH);
    public static final IntOption CHAT_KEY = new IntOption("key_chat", Input.Keys.T);
    public static final IntOption INVENTORY_KEY = new IntOption("key_inventory", Input.Keys.E);
    public static final IntOption BACK_KEY = new IntOption("key_back", Input.Keys.ESCAPE);
    public static final IntOption ABILITY_KEY = new IntOption("key_ability", Input.Keys.SPACE);
    public static final IntOption FULLSCREEN_KEY = new IntOption("key_fullscreen", Input.Keys.F11);

    public static boolean isMovementKey(int keycode) {
        return keycode == UP_KEY.value() || keycode == DOWN_KEY.value() || keycode == LEFT_KEY.value() || keycode == RIGHT_KEY.value();
    }
}
