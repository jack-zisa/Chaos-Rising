package dev.creoii.chaos.util.provider.booleanprovider;

public class ConstantBooleanProvider implements BooleanProvider {
    private final boolean value;

    public ConstantBooleanProvider(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean get(Context context) {
        return value;
    }

    @Override
    public ConstantBooleanProvider copy() {
        return this;
    }

    public ConstantBooleanProvider init(int startTime) {
        return this;
    }
}
