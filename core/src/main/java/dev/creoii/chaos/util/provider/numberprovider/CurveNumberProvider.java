package dev.creoii.chaos.util.provider.numberprovider;

public class CurveNumberProvider implements NumberProvider {
    private final NumberProvider start;
    private final NumberProvider end;
    private final NumberProvider duration;
    private final CurveType type;
    private Float value = null;
    private float progress;

    @Override
    public CurveNumberProvider copy() {
        return new CurveNumberProvider(start.copy(), end.copy(), duration.copy(), type);
    }

    public enum CurveType {
        LINEAR,
        EXPONENTIAL,
        EASE_IN,
        EASE_OUT
    }

    public CurveNumberProvider(NumberProvider start, NumberProvider end, NumberProvider duration, CurveType type) {
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.type = type;
    }

    public CurveNumberProvider init(int startTime) {
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
