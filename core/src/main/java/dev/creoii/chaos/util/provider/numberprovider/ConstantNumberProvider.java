package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ConstantNumberProvider(float value) implements NumberProvider {
    public static final ConstantNumberProvider ZERO = new ConstantNumberProvider(0);
    public static final ConstantNumberProvider ONE = new ConstantNumberProvider(1);
    public static final ConstantNumberProvider NEG_ONE = new ConstantNumberProvider(-1);
    public static final ConstantNumberProvider HALF = new ConstantNumberProvider(.5f);
    public static final MapCodec<ConstantNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.FLOAT.fieldOf("value").forGetter(ConstantNumberProvider::value)
        ).apply(instance, (value) -> {
            if (value <= 1 && value >= -1) {
                if (value == -1) return NEG_ONE;
                else if (value == 0) return ZERO;
                else if (value == .5f) return HALF;
                else return ONE;
            } else return new ConstantNumberProvider(value);
        });
    });

    @Override
    public Type getType() {
        return Type.CONSTANT;
    }

    @Override
    public Float get(Context context) {
        return value;
    }

    @Override
    public ConstantNumberProvider copy() {
        return this;
    }

    public ConstantNumberProvider init(int startTime) {
        return this;
    }
}
