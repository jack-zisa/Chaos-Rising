package dev.creoii.chaos.util.provider.floatprovider;

public class CycleFloatProvider implements FloatProvider {
    private final FloatProvider value;
    private final FloatProvider max;

    public CycleFloatProvider(FloatProvider value, FloatProvider max) {
        this.value = value;
        this.max = max;
    }

    @Override
    public Float get(Context context) {
        return value.get(context) % max.get(context);
    }

    @Override
    public FloatProvider copy() {
        return new CycleFloatProvider(value.copy(), max.copy());
    }

    @Override
    public FloatProvider init(int startTime) {
        value.init(startTime);
        max.init(startTime);
        return this;
    }
}
