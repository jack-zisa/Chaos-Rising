package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import java.util.List;
import java.util.function.Function;

public interface VecProvider extends Provider<Vector2> {
    Codec<VecProvider> DISPATCH_CODEC = Type.CODEC.dispatch(VecProvider::getType, type -> switch (type) {
        case BINARY -> BinaryVecProvider.CODEC;
        case ADD -> BinaryVecProvider.ADD_CODEC;
        case SUB -> BinaryVecProvider.SUB_CODEC;
        case MUL -> BinaryVecProvider.MUL_CODEC;
        case DIV -> BinaryVecProvider.DIV_CODEC;
        case MOD -> BinaryVecProvider.MOD_CODEC;
        case POW -> BinaryVecProvider.POW_CODEC;
        case CLAMP -> ClampVecProvider.CODEC;
        case COMPARISON -> ComparisonVecProvider.CODEC;
        case CONSTANT -> ConstantVecProvider.CODEC;
        case DIRECTION_TO -> DirectionToVecProvider.CODEC;
        case ENTITY -> EntityVecProvider.CODEC;
        case NORMALIZED -> NormalizedVecProvider.CODEC;
        case PERPENDICULAR -> PerpendicularVecProvider.CODEC;
        case RANDOM_BETWEEN -> RandomBetweenVecProvider.CODEC;
        case RELATIVE_TO -> RelativeToVecProvider.CODEC;
        case ROTATE_ANGLE -> RotateAngleVecProvider.CODEC;
        case ROTATED_OFFSET -> RotatedOffsetVecProvider.CODEC;
        case SOURCE_POS -> SourcePosVecProvider.CODEC;
        case UNARY -> UnaryVecProvider.CODEC;
        case SIN -> UnaryVecProvider.SIN_CODEC;
        case COS -> UnaryVecProvider.COS_CODEC;
        case TAN -> UnaryVecProvider.TAN_CODEC;
        case SQRT -> UnaryVecProvider.SQRT_CODEC;
        case CBRT -> UnaryVecProvider.CBRT_CODEC;
        case ABS -> UnaryVecProvider.ABS_CODEC;
    });

    Codec<VecProvider> CODEC = Codec.either(Codec.FLOAT.listOf(2, 2), DISPATCH_CODEC).xmap(either -> either.map(list -> new ConstantVecProvider(new ConstantNumberProvider(list.getFirst()), new ConstantNumberProvider(list.get(1))), Function.identity()),
        vecProvider -> {
            if (vecProvider instanceof ConstantVecProvider(NumberProvider x, NumberProvider y)) {
                if (x instanceof ConstantNumberProvider(float value) && y instanceof ConstantNumberProvider(float value1)) {
                    return Either.left(List.of(value, value1));
                }
                throw new IllegalArgumentException("Illegal value in raw VecProvider");
            } else {
                return Either.right(vecProvider);
            }
        }
    );

    Type getType();

    VecProvider copy();

    enum Type {
        BINARY, ADD, SUB, MUL, DIV, MOD, POW,
        CLAMP,
        COMPARISON,
        CONSTANT,
        DIRECTION_TO,
        ENTITY,
        NORMALIZED,
        PERPENDICULAR,
        RANDOM_BETWEEN,
        RELATIVE_TO,
        ROTATE_ANGLE,
        ROTATED_OFFSET,
        SOURCE_POS,
        UNARY, SIN, COS, TAN, SQRT, CBRT, ABS;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
