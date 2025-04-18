package dev.creoii.chaos;

import com.badlogic.gdx.Input;

public class OptionsManager {
    public final Option<Integer> FORWARDS_KEY = new Option<>("key_forwards", Input.Keys.W);
    public final Option<Integer> BACKWARDS_KEY = new Option<>("key_backwards", Input.Keys.S);
    public final Option<Integer> LEFT_KEY = new Option<>("key_left", Input.Keys.A);
    public final Option<Integer> RIGHT_KEY = new Option<>("key_right", Input.Keys.D);
    public final Option<Integer> DEBUG_KEY = new Option<>("key_right", Input.Keys.F3);
    public final Option<Integer> COMMAND_KEY = new Option<>("key_right", Input.Keys.SLASH);
    public final Option<Integer> INVENTORY_KEY = new Option<>("key_right", Input.Keys.E);
    public final Option<Integer> BACK_KEY = new Option<>("key_back", Input.Keys.ESCAPE);
    public final Option<Integer> ABILITY_KEY = new Option<>("key_ability", Input.Keys.SPACE);

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
