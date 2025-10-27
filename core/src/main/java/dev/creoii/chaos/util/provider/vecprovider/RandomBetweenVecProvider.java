package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record RandomBetweenVecProvider(VecProvider min, VecProvider max) implements VecProvider {
    public static final MapCodec<RandomBetweenVecProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("min").forGetter(RandomBetweenVecProvider::min),
            VecProvider.CODEC.fieldOf("max").forGetter(RandomBetweenVecProvider::max)
        ).apply(instance, RandomBetweenVecProvider::new);
    });

    @Override
    public Type getType() {
        return Type.RANDOM_BETWEEN;
    }

    @Override
    public Vector2 get(Context context) {
        Vector2 min = this.min.get(context);
        Vector2 max = this.max.get(context);
        float x = min.x + context.random().nextInt((int) Math.max(1, max.x - min.x));
        float y = min.y + context.random().nextInt((int) Math.max(1, max.y - min.y));
        return new Vector2(x, y);
    }

    @Override
    public RandomBetweenVecProvider copy() {
        return new RandomBetweenVecProvider(min.copy(), max.copy());
    }
}
