package dev.creoii.chaos.util.provider.phaseprovider;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.vecprovider.*;

public interface PhaseProvider extends Provider<Phase> {
    Codec<PhaseProvider> CODEC = Type.CODEC.dispatch(PhaseProvider::getType, type -> switch (type) {
        case RANDOM -> RandomPhaseProvider.CODEC;
        case OFFSET -> OffsetPhaseProvider.CODEC;
        case NEXT -> OffsetPhaseProvider.NEXT_CODEC;
        case PREV -> OffsetPhaseProvider.PREV_CODEC;
        case CURRENT -> OffsetPhaseProvider.CURRENT_CODEC;
        case TO -> ToPhaseProvider.CODEC;
    });

    Type getType();

    enum Type {
        RANDOM,
        OFFSET, NEXT, PREV, CURRENT,
        TO;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
