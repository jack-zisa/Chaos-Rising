package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record AngleNumberProvider(VecProvider a, VecProvider b) implements NumberProvider {
    public static final MapCodec<AngleNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(AngleNumberProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(AngleNumberProvider::b)
        ).apply(instance, AngleNumberProvider::new);
    });

    @Override
    public Type getType() {
        return Type.ANGLE;
    }

    @Override
    public Float get(Context context) {
        return a.get(context).angleDeg(b.get(context));
    }

    @Override
    public AngleNumberProvider copy() {
        return new AngleNumberProvider(a.copy(), b.copy());
    }

    public AngleNumberProvider init(int startTime) {
        return this;
    }
}
