package dev.creoii.chaos.util.provider.entityprovider;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.util.provider.Provider;

public interface EntityProvider extends Provider<Entity> {
    Codec<EntityProvider> CODEC = Type.CODEC.dispatch(EntityProvider::getType, type -> switch (type) {
        case SELF -> SelfEntityProvider.CODEC;
        case NEAREST_CHARACTER -> NearestCharacterEntityProvider.CODEC;
    });

    Type getType();

    enum Type {
        SELF,
        NEAREST_CHARACTER;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
