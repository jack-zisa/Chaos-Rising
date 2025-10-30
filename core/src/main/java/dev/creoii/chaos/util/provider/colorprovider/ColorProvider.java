package dev.creoii.chaos.util.provider.colorprovider;

import com.badlogic.gdx.graphics.Color;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.provider.Provider;

public interface ColorProvider extends Provider<Color> {
    Codec<ColorProvider> CODEC = Type.CODEC.dispatch(ColorProvider::getType, type -> switch (type) {
        case CONSTANT -> ConstantColorProvider.CODEC;
        case RANDOM -> RandomColorProvider.CODEC;
        case BLEND -> BlendColorProvider.CODEC;
        case COMPARISON -> ComparisonColorProvider.CODEC;
        case BINARY -> BinaryColorProvider.CODEC;
        case ADD -> BinaryColorProvider.ADD_CODEC;
        case SUB -> BinaryColorProvider.SUB_CODEC;
        case MUL -> BinaryColorProvider.MUL_CODEC;
        case DIV -> BinaryColorProvider.DIV_CODEC;
        case MOD -> BinaryColorProvider.MOD_CODEC;
        case POW -> BinaryColorProvider.POW_CODEC;
        case UNARY -> UnaryColorProvider.CODEC;
        case SIN -> UnaryColorProvider.SIN_CODEC;
        case COS -> UnaryColorProvider.COS_CODEC;
        case TAN -> UnaryColorProvider.TAN_CODEC;
        case SQRT -> UnaryColorProvider.SQRT_CODEC;
        case CBRT -> UnaryColorProvider.CBRT_CODEC;
        case ABS -> UnaryColorProvider.ABS_CODEC;
    });

    Type getType();

    enum Type {
        CONSTANT,
        RANDOM,
        BLEND,
        COMPARISON,
        BINARY, ADD, SUB, MUL, DIV, MOD, POW,
        UNARY, SIN, COS, TAN, SQRT, CBRT, ABS;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
