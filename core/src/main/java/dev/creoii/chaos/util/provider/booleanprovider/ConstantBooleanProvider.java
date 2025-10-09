package dev.creoii.chaos.util.provider.booleanprovider;

public record ConstantBooleanProvider(boolean value) implements BooleanProvider {
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
