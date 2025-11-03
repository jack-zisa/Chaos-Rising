package dev.creoii.chaos.util.provider.booleanprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.vecprovider.ConstantVecProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record InRangeBooleanProvider(NumberProvider distance2, VecProvider a, VecProvider b) implements BooleanProvider {
    public static final MapCodec<InRangeBooleanProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("distance2").forGetter(InRangeBooleanProvider::distance2),
            VecProvider.CODEC.fieldOf("a").forGetter(InRangeBooleanProvider::a),
            VecProvider.CODEC.fieldOf("b").forGetter(InRangeBooleanProvider::b)
        ).apply(instance, InRangeBooleanProvider::new);
    });

    @Override
    public Type getType() {
        return Type.IN_RANGE;
    }

    @Override
    public Boolean get(Context context) {
        return Math.abs(a.get(context).dst2(b.get(context))) <= distance2.get(context);
    }

    @Override
    public Provider<Boolean> optimize() {
        if (distance2 instanceof ConstantNumberProvider(float value) && a instanceof ConstantVecProvider(Vector2 pos) && b instanceof ConstantVecProvider(Vector2 pos1)) {
            return new ConstantBooleanProvider(Math.abs(pos.dst2(pos1)) <= value);
        }
        return BooleanProvider.super.optimize();
    }

    @Override
    public InRangeBooleanProvider copy() {
        return new InRangeBooleanProvider(distance2.copy(), a.copy(), b.copy());
    }

    public InRangeBooleanProvider init(int startTime) {
        distance2.init(startTime);
        return this;
    }
}
