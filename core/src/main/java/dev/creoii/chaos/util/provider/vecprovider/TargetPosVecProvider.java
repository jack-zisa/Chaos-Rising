package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

import javax.annotation.Nullable;

public record TargetPosVecProvider() implements VecProvider {
    private static final TargetPosVecProvider INSTANCE = new TargetPosVecProvider();
    public static final MapCodec<TargetPosVecProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.TARGET_POS;
    }

    @Override
    @Nullable
    public Vector2 get(ContextProvider context) {
        return context.has(ComponentTypes.TARGET_POS) ? context.get(ComponentTypes.TARGET_POS).cpy() : Vector2.Zero.cpy();
    }

    @Override
    public VecProvider copy() {
        return INSTANCE;
    }
}
