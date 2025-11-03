package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;

public record PerpendicularVecProvider(VecProvider value) implements VecProvider {
    public static final MapCodec<PerpendicularVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("value").forGetter(PerpendicularVecProvider::value)
        ).apply(instance, PerpendicularVecProvider::new);
    });

    @Override
    public Provider<Vector2> optimize() {
        if (value instanceof ConstantVecProvider(Vector2 pos)) {
            return new ConstantVecProvider(new Vector2(-pos.y, pos.x).nor().cpy());
        }
        return VecProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.PERPENDICULAR;
    }

    @Override
    public Vector2 get(Context context) {
        Vector2 vector2 = value.get(context);
        return new Vector2(-vector2.y, vector2.x).nor().cpy();
    }

    @Override
    public VecProvider copy() {
        return new PerpendicularVecProvider(value.copy());
    }
}
