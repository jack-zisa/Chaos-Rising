package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record RelativeToVecProvider(VecProvider parent, VecProvider offset) implements VecProvider {
    public static final MapCodec<RelativeToVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("parent").forGetter(RelativeToVecProvider::parent),
            VecProvider.CODEC.fieldOf("offset").forGetter(RelativeToVecProvider::offset)
        ).apply(instance, RelativeToVecProvider::new);
    });

    @Override
    public Type getType() {
        return Type.RELATIVE_TO;
    }

    @Override
    public Vector2 get(Context context) {
        return parent.get(context).add(offset.get(context));
    }

    @Override
    public VecProvider copy() {
        return new RelativeToVecProvider(parent.copy(), offset.copy());
    }
}
