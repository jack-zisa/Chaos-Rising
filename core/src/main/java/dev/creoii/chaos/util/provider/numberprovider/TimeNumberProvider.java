package dev.creoii.chaos.util.provider.numberprovider;

public record TimeNumberProvider() implements NumberProvider {
    @Override
    public Float get(Context context) {
        return (float) context.game().getGametime();
    }

    @Override
    public TimeNumberProvider copy() {
        return new TimeNumberProvider();
    }

    public TimeNumberProvider init(int startTime) {
        return this;
    }
}
