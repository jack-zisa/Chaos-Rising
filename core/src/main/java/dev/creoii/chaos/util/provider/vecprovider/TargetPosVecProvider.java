package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;

public record TargetPosVecProvider() implements VecProvider {
    private static final TargetPosVecProvider INSTANCE = new TargetPosVecProvider();
    public static final MapCodec<TargetPosVecProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.TARGET_POS;
    }

    @Override
    public Vector2 get(Context context) {
        return Vector2.Zero;
    }

    @Override
    public VecProvider copy() {
        return new TargetPosVecProvider();
    }
}
