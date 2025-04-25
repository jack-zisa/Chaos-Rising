package dev.creoii.chaos.util.provider.numberprovider;

public class ConstantNumberProvider implements NumberProvider {
    private final float value;

    public ConstantNumberProvider(float value) {
        this.value = value;
    }

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
