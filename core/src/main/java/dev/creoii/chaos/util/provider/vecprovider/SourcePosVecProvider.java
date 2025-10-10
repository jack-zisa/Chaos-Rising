package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;

public record SourcePosVecProvider() implements VecProvider {
    private static final SourcePosVecProvider INSTANCE = new SourcePosVecProvider();
    public static final MapCodec<SourcePosVecProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.SOURCE_POS;
    }

    @Override
    public Vector2 get(Context context) {
        return context.sourceEntity().getPos().cpy();
    }

    @Override
    public VecProvider copy() {
        return new SourcePosVecProvider();
    }
}
