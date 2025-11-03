package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Comparison;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public record NumberComparisonBooleanProvider(NumberProvider a, NumberProvider b, Comparison comparison) implements BooleanProvider {
    public static final MapCodec<NumberComparisonBooleanProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("a").forGetter(NumberComparisonBooleanProvider::a),
            NumberProvider.CODEC.fieldOf("b").forGetter(NumberComparisonBooleanProvider::b),
            Comparison.CODEC.fieldOf("comparison").orElse(Comparison.E).forGetter(NumberComparisonBooleanProvider::comparison)
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
    public Provider<Boolean> optimize() {
        if (a instanceof ConstantNumberProvider(float value) && b instanceof ConstantNumberProvider(float value1)) {
            return new ConstantBooleanProvider(switch (comparison) {
                case LT -> value < value1;
                case GT -> value > value1;
                case LTEQ -> value <= value1;
                case GTEQ -> value >= value1;
                case NE -> value != value1;
                case E -> value == value1;
            });
        }
        return BooleanProvider.super.optimize();
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
