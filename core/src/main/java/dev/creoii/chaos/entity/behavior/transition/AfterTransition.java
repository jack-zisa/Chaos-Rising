package dev.creoii.chaos.entity.behavior.transition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;

public record AfterTransition(NumberProvider after, PhaseProvider target) implements Transition {
    public static final MapCodec<AfterTransition> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("after").forGetter(AfterTransition::after),
            PhaseProvider.CODEC.fieldOf("target").forGetter(AfterTransition::target)
        ).apply(instance, AfterTransition::new);
    });

    @Override
    public Type getType() {
        return Type.AFTER;
    }

    @Override
    public boolean canTransition(Provider.Context context, int time) {
        return time >= after.get(context);
    }
}
