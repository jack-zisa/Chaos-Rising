package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Provider;

public record RotatedOffsetVecProvider(VecProvider from, VecProvider to, VecProvider offset) implements VecProvider {
    public static final MapCodec<RotatedOffsetVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("from").forGetter(RotatedOffsetVecProvider::from),
            VecProvider.CODEC.fieldOf("to").forGetter(RotatedOffsetVecProvider::to),
            VecProvider.CODEC.fieldOf("offset").forGetter(RotatedOffsetVecProvider::offset)
        ).apply(instance, RotatedOffsetVecProvider::new);
    });

    @Override
    public Provider<Vector2> optimize() {
        if (from instanceof ConstantVecProvider(Vector2 pos) && to instanceof ConstantVecProvider(Vector2 pos1) && offset instanceof ConstantVecProvider(Vector2 pos2)) {
            Vector2 direction = pos1.sub(pos);
            return new ConstantVecProvider(pos2.rotateRad(direction.angleRad()));
        }
        return VecProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.ROTATED_OFFSET;
    }

    @Override
    public Vector2 get(ContextProvider context) {
        Vector2 direction = to.get(context).sub(from.get(context));
        return offset.get(context).rotateRad(direction.angleRad());
    }

    @Override
    public VecProvider copy() {
        return new RotatedOffsetVecProvider(from.copy(), to.copy(), offset.copy());
    }
}
