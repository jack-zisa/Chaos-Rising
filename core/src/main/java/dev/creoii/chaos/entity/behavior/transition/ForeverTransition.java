package dev.creoii.chaos.entity.behavior.transition;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.phaseprovider.OffsetPhaseProvider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;

public record ForeverTransition(PhaseProvider target) implements Transition {
    public static final ForeverTransition INSTANCE = new ForeverTransition();
    public static final MapCodec<ForeverTransition> CODEC = MapCodec.unit(INSTANCE);

    public ForeverTransition() {
        this(OffsetPhaseProvider.CURRENT);
    }

    @Override
    public Type getType() {
        return Type.FOREVER;
    }

    @Override
    public boolean shouldTransition(Provider.Context context, int time) {
        return false;
    }
}
