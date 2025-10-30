package dev.creoii.chaos.util.provider.colorprovider;

import com.badlogic.gdx.graphics.Color;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record RandomColorProvider(List<ColorProvider> values) implements ColorProvider {
    public static final MapCodec<RandomColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ColorProvider.CODEC.listOf().fieldOf("values").forGetter(RandomColorProvider::values)
        ).apply(instance, RandomColorProvider::new)
    );

    @Override
    public Type getType() {
        return Type.RANDOM;
    }

    @Override
    public Color get(Context context) {
        return values.get(context.random().nextInt(values.size())).get(context);
    }
}
