package dev.creoii.chaos.util.provider.colorprovider;

import com.badlogic.gdx.graphics.Color;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import java.util.List;

public record BlendColorProvider(ColorProvider a, ColorProvider b, NumberProvider factor) implements ColorProvider {
    public static final MapCodec<BlendColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ColorProvider.CODEC.fieldOf("a").forGetter(BlendColorProvider::a),
            ColorProvider.CODEC.fieldOf("b").forGetter(BlendColorProvider::b),
            NumberProvider.CODEC.orElse(ConstantNumberProvider.HALF).fieldOf("factor").forGetter(BlendColorProvider::factor)
        ).apply(instance, BlendColorProvider::new)
    );

    @Override
    public Type getType() {
        return Type.BLEND;
    }

    @Override
    public Color get(Context context) {
        return a.get(context).lerp(b.get(context), factor.get(context));
    }
}
