package dev.creoii.chaos.util.provider.intprovider;

import dev.creoii.chaos.util.provider.floatprovider.FloatProvider;

public class CurveIntProvider implements IntProvider {
    private final IntProvider start;
    private final IntProvider end;
    private final FloatProvider duration;
    private final CurveType type;

    @Override
    public IntProvider copy() {
        return new CurveIntProvider(start.copy(), end.copy(), duration.copy(), type);
    }

    public enum CurveType {
        LINEAR,
        EXPONENTIAL,
        EASE_IN,
        EASE_OUT
    }

    public CurveIntProvider(IntProvider start, IntProvider end, FloatProvider duration, CurveType type) {
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.type = type;
    }

    @Override
    public Integer get(Context context) {
        float t = Math.min(context.startTime() / duration.get(context), 1f);
        int a = start.get(context);
        int b = end.get(context);

        return switch (type) {
            case LINEAR -> a + (b - a) * (int) t;
            case EXPONENTIAL -> a + (b - a) * (int) (t * t);
            case EASE_IN -> a + (b - a) * (int) (1 - Math.cos(t * Math.PI * .5f));
            case EASE_OUT -> a + (b - a) * (int) Math.sin(t * Math.PI * .5f);
        };
    }
}
