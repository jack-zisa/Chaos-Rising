package dev.creoii.chaos;

import com.badlogic.gdx.Input;

public final class OptionsManager {
    public static final Option<Integer> UP_KEY = new Option<>("key_forwards", Input.Keys.W);
    public static final Option<Integer> DOWN_KEY = new Option<>("key_backwards", Input.Keys.S);
    public static final Option<Integer> LEFT_KEY = new Option<>("key_left", Input.Keys.A);
    public static final Option<Integer> RIGHT_KEY = new Option<>("key_right", Input.Keys.D);
    public static final Option<Integer> DEBUG_KEY = new Option<>("key_debug", Input.Keys.F3);
    public static final Option<Integer> COMMAND_KEY = new Option<>("key_command", Input.Keys.SLASH);
    public static final Option<Integer> CHAT_KEY = new Option<>("key_chat", Input.Keys.T);
    public static final Option<Integer> INVENTORY_KEY = new Option<>("key_inventory", Input.Keys.E);
    public static final Option<Integer> BACK_KEY = new Option<>("key_back", Input.Keys.ESCAPE);
    public static final Option<Integer> ABILITY_KEY = new Option<>("key_ability", Input.Keys.SPACE);

    public static boolean isMovementKey(int keycode) {
        return keycode == UP_KEY.intValue() || keycode == DOWN_KEY.intValue() || keycode == LEFT_KEY.intValue() || keycode == RIGHT_KEY.intValue();
    }

    public static class Option<T extends Number> {
        private final String key;
        private Number value;

        public Option(String key) {
            this.key = key;
        }

        public Option(String key, Number initialValue) {
            this.key = key;
            value = initialValue;
        }

        public String getKey() {
            return key;
        }

        public int intValue() {
            return value.intValue();
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
