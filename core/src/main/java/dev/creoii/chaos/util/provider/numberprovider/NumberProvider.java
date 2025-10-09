package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.provider.Provider;

import java.util.function.Function;

public interface NumberProvider extends Provider<Float> {
    Codec<NumberProvider> DISPATCH_CODEC = Type.CODEC.dispatch(NumberProvider::getType, type -> switch (type) {
        case ANGLE -> AngleNumberProvider.CODEC;
        case BINARY -> BinaryNumberProvider.CODEC;
        case BULLET_INDEX -> BulletIndexNumberProvider.CODEC;
        case CLAMP -> ClampNumberProvider.CODEC;
        case COMPARISON -> ComparisonNumberProvider.CODEC;
        case CONSTANT -> ConstantNumberProvider.CODEC;
        case CURVE -> CurveNumberProvider.CODEC;
        case CYCLE -> CycleNumberProvider.CODEC;
        case DISTANCE_2 -> Distance2NumberProvider.CODEC;
        case DISTANCE -> DistanceNumberProvider.CODEC;
        case LENGTH_2 -> Length2NumberProvider.CODEC;
        case LENGTH -> LengthNumberProvider.CODEC;
        case RANDOM -> RandomNumberProvider.CODEC;
        case SPAWN_TIME -> SpawnTimeNumberProvider.CODEC;
        case STAT -> StatNumberProvider.CODEC;
        case TIME -> TimeNumberProvider.CODEC;
        case UNARY -> UnaryNumberProvider.CODEC;
    });

    Codec<NumberProvider> CODEC = Codec.either(Codec.FLOAT, DISPATCH_CODEC).xmap(
        either -> either.map(ConstantNumberProvider::new, Function.identity()),
        bp -> {
            if (bp instanceof ConstantNumberProvider(float value)) {
                return Either.left(value);
            } else return Either.right(bp);
        }
    );

    Type getType();

    NumberProvider copy();

    NumberProvider init(int startTime);

    default int getInt(Context context) {
        return Math.round(get(context));
    }

    enum Type {
        ANGLE,
        BINARY,
        BULLET_INDEX,
        CLAMP,
        COMPARISON,
        CONSTANT,
        CURVE,
        CYCLE,
        DISTANCE_2,
        DISTANCE,
        LENGTH_2,
        LENGTH,
        RANDOM,
        SPAWN_TIME,
        STAT,
        TIME,
        UNARY;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
