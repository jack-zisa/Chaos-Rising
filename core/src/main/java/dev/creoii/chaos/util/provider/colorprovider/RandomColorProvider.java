package dev.creoii.chaos.util.provider.colorprovider;

import com.badlogic.gdx.graphics.Color;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

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
    public Color get(ContextProvider context) {
        if (context.has(ComponentTypes.RANDOM)) {
            return values.get(context.get(ComponentTypes.RANDOM).nextInt(values.size())).get(context);
        }
        return null;
    }
}
