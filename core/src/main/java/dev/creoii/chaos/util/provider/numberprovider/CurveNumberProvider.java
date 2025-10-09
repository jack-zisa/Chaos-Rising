package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class CurveNumberProvider implements NumberProvider {
    public static final MapCodec<CurveNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("start").forGetter(CurveNumberProvider::getStart),
            NumberProvider.CODEC.fieldOf("end").forGetter(CurveNumberProvider::getEnd),
            NumberProvider.CODEC.fieldOf("duration").forGetter(CurveNumberProvider::getDuration),
            CurveType.CODEC.fieldOf("max").forGetter(CurveNumberProvider::getCurveType)
        ).apply(instance, CurveNumberProvider::new);
    });
    private final NumberProvider start;
    private final NumberProvider end;
    private final NumberProvider duration;
    private final CurveType curveType;
    private Float value = null;
    private float progress;

    @Override
    public Type getType() {
        return Type.CURVE;
    }

    public NumberProvider getStart() {
        return start;
    }

    public NumberProvider getEnd() {
        return end;
    }

    public NumberProvider getDuration() {
        return duration;
    }

    public CurveType getCurveType() {
        return curveType;
    }

    @Override
    public CurveNumberProvider copy() {
        return new CurveNumberProvider(start.copy(), end.copy(), duration.copy(), curveType);
    }

    public enum CurveType {
        LINEAR,
        EXPONENTIAL,
        EASE_IN,
        EASE_OUT;

        public static final Codec<CurveType> CODEC = Codec.STRING.xmap(s -> CurveType.valueOf(s.toUpperCase()), curveType -> curveType.name().toLowerCase());
    }

    public CurveNumberProvider(NumberProvider start, NumberProvider end, NumberProvider duration, CurveType curveType) {
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.curveType = curveType;
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

        float factor = switch (curveType) {
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
