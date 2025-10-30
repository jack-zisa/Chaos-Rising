package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record DirectionToVecProvider(VecProvider from, VecProvider to) implements VecProvider {
    public static final MapCodec<DirectionToVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("from").forGetter(DirectionToVecProvider::from),
            VecProvider.CODEC.fieldOf("to").forGetter(DirectionToVecProvider::to)
        ).apply(instance, DirectionToVecProvider::new);
    });

    @Override
    public Type getType() {
        return Type.DIRECTION_TO;
    }

    @Override
    public Vector2 get(Context context) {
        return to.get(context).sub(from.get(context)).nor().cpy();
    }

    @Override
    public VecProvider copy() {
        return new DirectionToVecProvider(from.copy(), to.copy());
    }
}
