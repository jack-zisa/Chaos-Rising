package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

public record SourceVecProvider() implements VecProvider {
    public static final SourceVecProvider INSTANCE = new SourceVecProvider();
    public static final MapCodec<SourceVecProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.SOURCE;
    }

    @Override
    public Vector2 get(ContextProvider context) {
        if (context.has(ComponentTypes.ENTITY))
            return context.get(ComponentTypes.ENTITY).getPos().cpy();
        else if (context.has(ComponentTypes.POS))
            return context.get(ComponentTypes.POS).cpy();
        return null;
    }

    @Override
    public VecProvider copy() {
        return INSTANCE;
    }
}
