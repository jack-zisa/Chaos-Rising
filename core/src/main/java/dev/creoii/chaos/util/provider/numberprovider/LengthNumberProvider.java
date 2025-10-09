package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record LengthNumberProvider(VecProvider vec) implements NumberProvider {
    public static final MapCodec<LengthNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("vec").forGetter(LengthNumberProvider::vec)
        ).apply(instance, LengthNumberProvider::new);
    });

    @Override
    public Type getType() {
        return Type.LENGTH;
    }

    @Override
    public Float get(Context context) {
        return vec.get(context).len();
    }

    @Override
    public LengthNumberProvider copy() {
        return new LengthNumberProvider(vec.copy());
    }

    public LengthNumberProvider init(int startTime) {
        return this;
    }
}
