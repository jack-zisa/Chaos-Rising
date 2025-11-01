package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;

public record SourceVecProvider() implements VecProvider {
    public static final SourceVecProvider INSTANCE = new SourceVecProvider();
    public static final MapCodec<SourceVecProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.SOURCE;
    }

    @Override
    public Vector2 get(Context context) {
        return context.sourceEntity().getPos().cpy();
    }

    @Override
    public VecProvider copy() {
        return INSTANCE;
    }
}
