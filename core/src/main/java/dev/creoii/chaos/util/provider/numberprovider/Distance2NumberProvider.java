package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record Distance2NumberProvider(VecProvider a, VecProvider b) implements NumberProvider {
    public static final MapCodec<Distance2NumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(Distance2NumberProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(Distance2NumberProvider::b)
        ).apply(instance, Distance2NumberProvider::new);
    });

    @Override
    public Type getType() {
        return Type.DISTANCE_2;
    }

    @Override
    public Float get(Context context) {
        return a.get(context).dst2(b.get(context));
    }

    @Override
    public Distance2NumberProvider copy() {
        return new Distance2NumberProvider(a.copy(), b.copy());
    }

    public Distance2NumberProvider init(int startTime) {
        return this;
    }
}
