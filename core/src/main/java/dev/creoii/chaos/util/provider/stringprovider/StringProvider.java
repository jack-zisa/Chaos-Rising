package dev.creoii.chaos.util.provider.stringprovider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.provider.Provider;

import java.util.function.Function;

public interface StringProvider extends Provider<String> {
    Codec<StringProvider> TYPE_CODEC = Type.CODEC.dispatch(StringProvider::getType, type -> switch (type) {
        case CONSTANT -> ConstantStringProvider.CODEC;
        case RANDOM -> RandomStringProvider.CODEC;
        case CONCAT -> ConcatStringProvider.CODEC;
    });
    Codec<StringProvider> CODEC = Codec.either(Codec.STRING, TYPE_CODEC).xmap(either -> {
        return either.map(ConstantStringProvider::new, Function.identity());
    }, Either::right);

    Type getType();

    enum Type {
        CONSTANT,
        RANDOM,
        CONCAT;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
