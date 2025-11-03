package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;

public record NormalizedVecProvider(VecProvider value) implements VecProvider {
    public static final MapCodec<NormalizedVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("value").forGetter(NormalizedVecProvider::value)
        ).apply(instance, NormalizedVecProvider::new);
    });

    @Override
    public Provider<Vector2> optimize() {
        if (value instanceof ConstantVecProvider(Vector2 pos)) {
            return new ConstantVecProvider(pos.nor().cpy());
        }
        return VecProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.NORMALIZED;
    }

    @Override
    public Vector2 get(Context context) {
        return value.get(context).nor().cpy();
    }

    @Override
    public VecProvider copy() {
        return new NormalizedVecProvider(value.copy());
    }
}
