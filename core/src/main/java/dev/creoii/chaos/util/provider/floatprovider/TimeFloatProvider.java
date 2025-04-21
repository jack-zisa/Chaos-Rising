package dev.creoii.chaos.util.provider.floatprovider;

public class TimeFloatProvider implements FloatProvider {
    @Override
    public Float get(Context context) {
        return (float) context.game().getGametime();
    }

    @Override
    public TimeFloatProvider copy() {
        return new TimeFloatProvider();
    }

    public TimeFloatProvider init(int startTime) {
        return this;
    }
}
