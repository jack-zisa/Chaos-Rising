package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Provider;

public record DirectionToVecProvider(VecProvider from, VecProvider to) implements VecProvider {
    public static final MapCodec<DirectionToVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("from").forGetter(DirectionToVecProvider::from),
            VecProvider.CODEC.fieldOf("to").forGetter(DirectionToVecProvider::to)
        ).apply(instance, DirectionToVecProvider::new);
    });

    @Override
    public Provider<Vector2> optimize() {
        if (from instanceof ConstantVecProvider(Vector2 pos) && to instanceof ConstantVecProvider(Vector2 pos1)) {
            return new ConstantVecProvider(pos1.sub(pos).nor().cpy());
        }
        return VecProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.DIRECTION_TO;
    }

    @Override
    public Vector2 get(ContextProvider context) {
        return to.get(context).sub(from.get(context)).nor().cpy();
    }

    @Override
    public VecProvider copy() {
        return new DirectionToVecProvider(from.copy(), to.copy());
    }
}
