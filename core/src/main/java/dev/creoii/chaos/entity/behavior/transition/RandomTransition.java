package dev.creoii.chaos.entity.behavior.transition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;
import dev.creoii.chaos.util.provider.phaseprovider.PhaseProvider;

public record RandomTransition(NumberProvider chance, PhaseProvider target) implements Transition {
    public static final MapCodec<RandomTransition> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("chance").forGetter(RandomTransition::chance),
            PhaseProvider.CODEC.fieldOf("target").forGetter(RandomTransition::target)
        ).apply(instance, (chance, target) -> new RandomTransition((NumberProvider) chance.optimize(), (PhaseProvider) target.optimize()));
    });

    @Override
    public Type getType() {
        return Type.RANDOM;
    }

    @Override
    public boolean shouldTransition(ContextProvider context, int time) {
        if (context.has(ComponentTypes.RANDOM)) {
            return context.get(ComponentTypes.RANDOM).nextInt(100) <= chance.get(context);
        }
        return true;
    }
}
