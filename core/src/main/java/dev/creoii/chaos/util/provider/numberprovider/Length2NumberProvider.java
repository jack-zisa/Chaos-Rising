package dev.creoii.chaos.util.provider.numberprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record Length2NumberProvider(VecProvider vec) implements NumberProvider {
    public static final MapCodec<Length2NumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("vec").forGetter(Length2NumberProvider::vec)
        ).apply(instance, Length2NumberProvider::new);
    });

    @Override
    public Provider<Float> optimize() {
        if (vec instanceof ConstantVecProvider(Vector2 pos)) {
            return new ConstantNumberProvider(pos.len2());
        }
        return NumberProvider.super.optimize();
    }

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
