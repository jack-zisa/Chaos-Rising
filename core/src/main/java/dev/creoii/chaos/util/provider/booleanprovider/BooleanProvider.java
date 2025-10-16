package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.provider.Provider;

import java.util.function.Function;

public interface BooleanProvider extends Provider<Boolean> {
    Codec<BooleanProvider> DISPATCH_CODEC = Type.CODEC.dispatch(
        BooleanProvider::getType,
        type -> switch (type) {
            case BETWEEN -> BetweenBooleanProvider.CODEC;
            case BINARY -> BinaryBooleanProvider.CODEC;
            case AND -> BinaryBooleanProvider.AND_CODEC;
            case OR -> BinaryBooleanProvider.OR_CODEC;
            case XOR -> BinaryBooleanProvider.XOR_CODEC;
            case CONSTANT -> ConstantBooleanProvider.CODEC;
            case HAS_EFFECT -> HasEffectBooleanProvider.CODEC;
            case IS_CLASS -> IsClassBooleanProvider.CODEC;
            case NOT -> NotBooleanProvider.CODEC;
            case NUMBER_COMPARISON -> NumberComparisonBooleanProvider.CODEC;
            case IN_RANGE -> InRangeBooleanProvider.CODEC;
            case RANDOM -> RandomBooleanProvider.CODEC;
        }
    );

    Codec<BooleanProvider> CODEC = Codec.either(Codec.BOOL, DISPATCH_CODEC).xmap(
        either -> either.map(ConstantBooleanProvider::new, Function.identity()),
        bp -> {
            if (bp instanceof ConstantBooleanProvider(boolean value)) {
                return Either.left(value);
            } else return Either.right(bp);
        }
    );

    Type getType();

    BooleanProvider copy();

    BooleanProvider init(int startTime);

    enum Type {
        BETWEEN,
        BINARY, AND, OR, XOR,
        CONSTANT,
        HAS_EFFECT,
        IS_CLASS,
        NOT,
        NUMBER_COMPARISON,
        IN_RANGE,
        RANDOM;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
