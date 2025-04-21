package dev.creoii.chaos.util.provider.intprovider;

public class ConstantIntProvider implements IntProvider {
    private final int value;

    public ConstantIntProvider(int value) {
        this.value = value;
    }

    @Override
    public Integer get(Context context) {
        return value;
    }

    @Override
    public IntProvider copy() {
        return new ConstantIntProvider(value);
    }
}
