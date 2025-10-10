package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CycleNumberProvider(NumberProvider value, NumberProvider max) implements NumberProvider {
    public static final MapCodec<CycleNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(CycleNumberProvider::value),
            NumberProvider.CODEC.fieldOf("max").forGetter(CycleNumberProvider::max)
        ).apply(instance, CycleNumberProvider::new);
    });

    @Override
    public Type getType() {
        return Type.CYCLE;
    }

    @Override
    public Float get(Context context) {
        return value.get(context) % max.get(context);
    }

    @Override
    public NumberProvider copy() {
        return new CycleNumberProvider(value.copy(), max.copy());
    }

    @Override
    public NumberProvider init(int startTime) {
        value.init(startTime);
        max.init(startTime);
        return this;
    }
}
