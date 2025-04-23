package dev.creoii.chaos.util.provider.numberprovider;

public class RandomNumberProvider implements NumberProvider {
    private final NumberProvider min, max;

    public RandomNumberProvider(NumberProvider min, NumberProvider max) {
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
    public RandomNumberProvider copy() {
        return new RandomNumberProvider(min.copy(), max.copy());
    }

    @Override
    public RandomNumberProvider init(int startTime) {
        min.init(startTime);
        max.init(startTime);
        return this;
    }
}

