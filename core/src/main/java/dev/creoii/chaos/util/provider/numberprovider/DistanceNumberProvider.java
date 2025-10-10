package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record DistanceNumberProvider(VecProvider a, VecProvider b) implements NumberProvider {
    public static final MapCodec<DistanceNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(DistanceNumberProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(DistanceNumberProvider::b)
        ).apply(instance, DistanceNumberProvider::new);
    });

    @Override
    public Type getType() {
        return Type.DISTANCE;
    }

    @Override
    public Float get(Context context) {
        return a.get(context).dst(b.get(context));
    }

    @Override
    public DistanceNumberProvider copy() {
        return new DistanceNumberProvider(a.copy(), b.copy());
    }

    public DistanceNumberProvider init(int startTime) {
        return this;
    }
}
