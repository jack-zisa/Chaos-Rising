package dev.creoii.chaos.util.provider.floatprovider;

public class RandomFloatProvider implements FloatProvider {
    private final FloatProvider min, max;

    public RandomFloatProvider(FloatProvider min, FloatProvider max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Float get(Context context) {
        float max = this.max.get(context);
        float min = this.min.get(context);
        return context.random().nextFloat() * (max - min) + min;
    }

    @Override
    public RandomFloatProvider copy() {
        return new RandomFloatProvider(min.copy(), max.copy());
    }

    @Override
    public RandomFloatProvider init(int startTime) {
        min.init(startTime);
        max.init(startTime);
        return this;
    }
}

