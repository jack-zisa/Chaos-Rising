package dev.creoii.chaos.entity.behavior.transition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;

public record ConditionTransition(BooleanProvider condition, PhaseProvider target) implements Transition {
    public static final MapCodec<ConditionTransition> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            BooleanProvider.CODEC.fieldOf("condition").forGetter(ConditionTransition::condition),
            PhaseProvider.CODEC.fieldOf("target").forGetter(ConditionTransition::target)
        ).apply(instance, ConditionTransition::new);
    });

    @Override
    public Type getType() {
        return Type.CONDITION;
    }

    @Override
    public boolean shouldTransition(Provider.Context context, int time) {
        return condition.get(context);
    }
}
