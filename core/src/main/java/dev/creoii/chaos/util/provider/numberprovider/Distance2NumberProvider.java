package dev.creoii.chaos.util.provider.numberprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record Distance2NumberProvider(VecProvider a, VecProvider b) implements NumberProvider {
    public static final MapCodec<Distance2NumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(Distance2NumberProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(Distance2NumberProvider::b)
        ).apply(instance, Distance2NumberProvider::new);
    });

    @Override
    public Provider<Float> optimize() {
        if (a instanceof ConstantVecProvider(Vector2 pos) && b instanceof ConstantVecProvider(Vector2 pos1)) {
            return new ConstantNumberProvider(pos.dst2(pos1));
        }
        return NumberProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.DISTANCE_2;
    }

    @Override
    public Float get(Context context) {
        return a.get(context).dst2(b.get(context));
    }

    @Override
    public Distance2NumberProvider copy() {
        return new Distance2NumberProvider(a.copy(), b.copy());
    }

    public Distance2NumberProvider init(int startTime) {
        return this;
    }
}
