package dev.creoii.chaos.util.provider.floatprovider;

public class ConstantFloatProvider implements FloatProvider {
    private final float value;

    public ConstantFloatProvider(float value) {
        this.value = value;
    }

    @Override
    public Float get(Context context) {
        return value;
    }

    @Override
    public ConstantFloatProvider copy() {
        return new ConstantFloatProvider(value);
    }

    public ConstantFloatProvider init(int startTime) {
        return this;
    }
}
