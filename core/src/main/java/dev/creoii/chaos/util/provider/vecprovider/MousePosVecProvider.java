package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

import javax.annotation.Nullable;

public record MousePosVecProvider() implements VecProvider {
    private static final MousePosVecProvider INSTANCE = new MousePosVecProvider();
    public static final MapCodec<MousePosVecProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.MOUSE_POS;
    }

    @Override
    @Nullable
    public Vector2 get(ContextProvider context) {
        return context.has(ComponentTypes.MOUSE_POS) ? context.get(ComponentTypes.MOUSE_POS) : null;
    }

    @Override
    public VecProvider copy() {
        return INSTANCE;
    }
}
