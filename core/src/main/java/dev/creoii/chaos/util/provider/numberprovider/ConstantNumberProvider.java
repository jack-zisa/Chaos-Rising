package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ConstantNumberProvider(float value) implements NumberProvider {
    public static final ConstantNumberProvider ZERO = new ConstantNumberProvider(0);
    public static final ConstantNumberProvider ONE = new ConstantNumberProvider(1);
    public static final ConstantNumberProvider NEG_ONE = new ConstantNumberProvider(-1);
    public static final MapCodec<ConstantNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.FLOAT.fieldOf("value").forGetter(ConstantNumberProvider::value)
        ).apply(instance, ConstantNumberProvider::new);
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
