package dev.creoii.chaos.entity.behavior.transition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;

public record ThresholdTransition(NumberProvider value, NumberProvider threshold, PhaseProvider target) implements Transition {
    public static final MapCodec<ThresholdTransition> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("value").forGetter(ThresholdTransition::value),
            NumberProvider.CODEC.fieldOf("threshold").forGetter(ThresholdTransition::threshold),
            PhaseProvider.CODEC.fieldOf("target").forGetter(ThresholdTransition::target)
        ).apply(instance, (value, threshold, target) -> new ThresholdTransition((NumberProvider) value.optimize(), (NumberProvider) threshold.optimize(), (PhaseProvider) target.optimize()));
    });

    @Override
    public Type getType() {
        return Type.THRESHOLD;
    }

    @Override
    public boolean shouldTransition(Provider.Context context, int time) {
        return value.get(context) <= threshold.get(context);
    }
}
