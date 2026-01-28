package dev.creoii.chaos.entity.behavior.transition;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.phaseprovider.OffsetPhaseProvider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;

public record NeverTransition(PhaseProvider target) implements Transition {
    public static final NeverTransition INSTANCE = new NeverTransition();
    public static final MapCodec<NeverTransition> CODEC = MapCodec.unit(INSTANCE);

    public NeverTransition() {
        this(OffsetPhaseProvider.CURRENT);
    }

    @Override
    public Type getType() {
        return Type.NEVER;
    }

    @Override
    public boolean shouldTransition(ContextProvider context, int time) {
        return false;
    }
}
