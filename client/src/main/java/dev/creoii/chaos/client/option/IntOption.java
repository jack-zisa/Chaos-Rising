package dev.creoii.chaos.client.option;

public class IntOption implements Option<Integer> {
    private final String key;
    private int value;

    public IntOption(String key) {
        this.key = key;
    }

    public IntOption(String key, int defaultValue) {
        this.key = key;
        value = defaultValue;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public Integer value() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }
}
