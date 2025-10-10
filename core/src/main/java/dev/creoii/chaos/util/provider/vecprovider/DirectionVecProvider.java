package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record DirectionVecProvider(VecProvider from, VecProvider to) implements VecProvider {
    public static final MapCodec<DirectionVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("from").forGetter(DirectionVecProvider::from),
            VecProvider.CODEC.fieldOf("to").forGetter(DirectionVecProvider::to)
        ).apply(instance, DirectionVecProvider::new);
    });

    @Override
    public Type getType() {
        return Type.DIRECTION;
    }

    @Override
    public Vector2 get(Context context) {
        return to.get(context).sub(from.get(context));
    }

    @Override
    public VecProvider copy() {
        return new DirectionVecProvider(from.copy(), to.copy());
    }
}
