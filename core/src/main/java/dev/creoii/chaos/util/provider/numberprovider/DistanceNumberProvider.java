package dev.creoii.chaos.util.provider.numberprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record DistanceNumberProvider(VecProvider a, VecProvider b) implements NumberProvider {
    public static final MapCodec<DistanceNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(DistanceNumberProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(DistanceNumberProvider::b)
        ).apply(instance, DistanceNumberProvider::new);
    });

    @Override
    public Provider<Float> optimize() {
        if (a instanceof ConstantVecProvider(Vector2 pos) && b instanceof ConstantVecProvider(Vector2 pos1)) {
            return new ConstantNumberProvider(pos.dst(pos1));
        }
        return NumberProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.DISTANCE;
    }

    @Override
    public Float get(ContextProvider context) {
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
