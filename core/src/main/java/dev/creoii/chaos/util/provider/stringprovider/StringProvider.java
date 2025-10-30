package dev.creoii.chaos.util.provider.stringprovider;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.util.provider.Provider;

public interface StringProvider extends Provider<String> {
    Codec<StringProvider> CODEC = Type.CODEC.dispatch(StringProvider::getType, type -> switch (type) {
        case CONSTANT -> ConstantStringProvider.CODEC;
        case RANDOM -> RandomStringProvider.CODEC;
        case CONCAT -> ConcatStringProvider.CODEC;
    });

    Type getType();

    enum Type {
        CONSTANT,
        RANDOM,
        CONCAT;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
