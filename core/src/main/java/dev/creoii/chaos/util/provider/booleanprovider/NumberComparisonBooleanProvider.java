package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Comparison;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public record NumberComparisonBooleanProvider(NumberProvider a, NumberProvider b, Comparison comparison) implements BooleanProvider {
    public static final MapCodec<NumberComparisonBooleanProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("a").forGetter(NumberComparisonBooleanProvider::a),
            NumberProvider.CODEC.fieldOf("b").forGetter(NumberComparisonBooleanProvider::b),
            Comparison.CODEC.fieldOf("comparison").forGetter(NumberComparisonBooleanProvider::comparison)
        ).apply(instance, NumberComparisonBooleanProvider::new);
    });

    @Override
    public Type getType() {
        return Type.NUMBER_COMPARISON;
    }

    @Override
    public Boolean get(Context context) {
        float av = a.get(context), bv = b.get(context);
        return switch (comparison) {
            case LT -> av < bv;
            case GT -> av > bv;
            case LTEQ -> av <= bv;
            case GTEQ -> av >= bv;
            case NE -> av != bv;
            case E -> av == bv;
        };
    }

    @Override
    public NumberComparisonBooleanProvider copy() {
        return new NumberComparisonBooleanProvider(a.copy(), b.copy(), comparison);
    }

    public NumberComparisonBooleanProvider init(int startTime) {
        a.init(startTime);
        b.init(startTime);
        return this;
    }
}
