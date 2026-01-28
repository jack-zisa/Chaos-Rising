package dev.creoii.chaos.entity.behavior.transition;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;

public interface Transition {
    Codec<Transition> CODEC = Transition.Type.CODEC.dispatch(Transition::getType, type -> switch (type) {
        case AFTER -> AfterTransition.CODEC;
        case NEVER -> NeverTransition.CODEC;
        case RANDOM -> RandomTransition.CODEC;
        case CONDITION -> ConditionTransition.CODEC;
        case BINARY -> BinaryTransition.CODEC;
        case AND -> BinaryTransition.AND_CODEC;
        case OR -> BinaryTransition.OR_CODEC;
        case XOR -> BinaryTransition.XOR_CODEC;
        case THRESHOLD -> ThresholdTransition.CODEC;
    });

    Type getType();

    PhaseProvider target();

    boolean shouldTransition(ContextProvider context, int time);

    default Phase getTarget(ContextProvider context) {
        return target().get(context);
    }

    enum Type {
        AFTER,
        NEVER,
        RANDOM,
        CONDITION,
        BINARY, AND, OR, XOR,
        THRESHOLD;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
