package dev.creoii.chaos.entity.behavior.transition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;

public class AfterTransition implements Transition {
    public static final MapCodec<AfterTransition> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("after").forGetter(AfterTransition::after),
            PhaseProvider.CODEC.fieldOf("target").forGetter(AfterTransition::target)
        ).apply(instance, (after, target) -> new AfterTransition((NumberProvider) after.optimize(), (PhaseProvider) target.optimize()));
    });

    private final NumberProvider after;
    private final PhaseProvider target;
    private int startTime;

    public AfterTransition(NumberProvider after, PhaseProvider target) {
        this.after = after;
        this.target = target;
    }

    public NumberProvider after() {
        return after;
    }

    public PhaseProvider target() {
        return target;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public Type getType() {
        return Type.AFTER;
    }

    @Override
    public boolean shouldTransition(Provider.Context context, int time) {
        return time - startTime >= after.get(context);
    }
}
