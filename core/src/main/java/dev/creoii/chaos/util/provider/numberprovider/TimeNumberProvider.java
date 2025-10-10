package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;

public record TimeNumberProvider() implements NumberProvider {
    private static final TimeNumberProvider INSTANCE = new TimeNumberProvider();
    public static final MapCodec<TimeNumberProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.TIME;
    }

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
