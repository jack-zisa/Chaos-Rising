package dev.creoii.chaos.util.provider.numberprovider;

public record ConstantNumberProvider(float value) implements NumberProvider {
    @Override
    public Float get(Context context) {
        return value;
    }

    @Override
    public ConstantNumberProvider copy() {
        return this;
    }

    public ConstantNumberProvider init(int startTime) {
        return this;
    }
}
