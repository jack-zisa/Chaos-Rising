package dev.creoii.chaos.entity.behavior.transition;

import com.mojang.serialization.Codec;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;

public interface Transition {
    Codec<Transition> CODEC = Transition.Type.CODEC.dispatch(Transition::getType, type -> switch (type) {
        case AFTER -> AfterTransition.CODEC;
        case FOREVER -> ForeverTransition.CODEC;
        case RANDOM -> RandomTransition.CODEC;
        case CONDITION -> ConditionTransition.CODEC;
        case THRESHOLD -> ThresholdTransition.CODEC;
    });

    Type getType();

    PhaseProvider target();

    boolean shouldTransition(Provider.Context context, int time);

    default Phase getTarget(Provider.Context context) {
        return target().get(context);
    }

    enum Type {
        AFTER,
        FOREVER,
        RANDOM,
        CONDITION,
        THRESHOLD;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
