package dev.creoii.chaos.util.provider.numberprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record AngleNumberProvider(VecProvider a, VecProvider b) implements NumberProvider {
    public static final MapCodec<AngleNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            VecProvider.CODEC.fieldOf("a").forGetter(AngleNumberProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(AngleNumberProvider::b)
        ).apply(instance, AngleNumberProvider::new);
    });

    @Override
    public Provider<Float> optimize() {
        if (a instanceof ConstantVecProvider(Vector2 pos) && b instanceof ConstantVecProvider(Vector2 pos1)) {
            return new ConstantNumberProvider(pos.angleDeg(pos1));
        }
        return NumberProvider.super.optimize();
    }

    @Override
    public Type getType() {
        return Type.ANGLE;
    }

    @Override
    public Float get(ContextProvider context) {
        return a.get(context).angleDeg(b.get(context));
    }

    @Override
    public AngleNumberProvider copy() {
        return new AngleNumberProvider(a.copy(), b.copy());
    }

    public AngleNumberProvider init(int startTime) {
        return this;
    }
}
