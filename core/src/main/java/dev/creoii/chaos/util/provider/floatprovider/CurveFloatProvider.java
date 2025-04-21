package dev.creoii.chaos.util.provider.floatprovider;

public class CurveFloatProvider implements FloatProvider {
    private final FloatProvider start;
    private final FloatProvider end;
    private final FloatProvider duration;
    private final CurveType type;
    private Float value = null;
    private float progress;

    @Override
    public CurveFloatProvider copy() {
        return new CurveFloatProvider(start.copy(), end.copy(), duration.copy(), type);
    }

    public enum CurveType {
        LINEAR,
        EXPONENTIAL,
        EASE_IN,
        EASE_OUT
    }

    public CurveFloatProvider(FloatProvider start, FloatProvider end, FloatProvider duration, CurveType type) {
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.type = type;
    }

    public CurveFloatProvider init(int startTime) {
        start.init(startTime);
        end.init(startTime);
        duration.init(startTime);
        return this;
    }

    @Override
    public Float get(Context context) {
        if (value == null) {
            value = start.get(context);
            progress = 0f;
        }

        float duration = this.duration.get(context);
        if (duration <= 0f)
            return end.get(context);

        progress += 1f / duration;
        if (progress > 1f)
            progress = 1f;

        float factor = switch (type) {
            case LINEAR -> progress;
            case EXPONENTIAL -> progress * progress;
            case EASE_IN -> 1f - (float) Math.cos(progress * Math.PI * 0.5f);
            case EASE_OUT -> (float) Math.sin(progress * Math.PI * 0.5f);
        };

        float startVal = start.get(context);
        float endVal = end.get(context);
        value = startVal + (endVal - startVal) * factor;
        return value;
    }
}
