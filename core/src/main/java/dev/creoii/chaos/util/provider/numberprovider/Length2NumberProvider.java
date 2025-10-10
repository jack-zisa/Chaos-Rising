package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record Length2NumberProvider(VecProvider vec) implements NumberProvider {
    public static final MapCodec<Length2NumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("vec").forGetter(Length2NumberProvider::vec)
        ).apply(instance, Length2NumberProvider::new);
    });

    @Override
    public Type getType() {
        return Type.LENGTH_2;
    }

    @Override
    public Float get(Context context) {
        return vec.get(context).len2();
    }

    @Override
    public Length2NumberProvider copy() {
        return new Length2NumberProvider(vec.copy());
    }

    public Length2NumberProvider init(int startTime) {
        return this;
    }
}
