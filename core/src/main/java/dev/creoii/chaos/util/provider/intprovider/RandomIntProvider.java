package dev.creoii.chaos.util.provider.intprovider;

public class RandomIntProvider implements IntProvider {
    private final IntProvider min, max;

    public RandomIntProvider(IntProvider min, IntProvider max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer get(Context context) {
        int max = this.max.get(context);
        int min = this.min.get(context);
        return context.random().nextInt(max - min + 1) + min;
    }

    @Override
    public IntProvider copy() {
        return new RandomIntProvider(min.copy(), max.copy());
    }
}

