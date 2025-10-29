package dev.creoii.chaos.util.provider.phaseprovider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.entity.behavior.MultiBehavior;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import javax.annotation.Nullable;

public record ToPhaseProvider(Either<String, NumberProvider> to) implements PhaseProvider {
    public static final MapCodec<ToPhaseProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.either(Codec.STRING, NumberProvider.CODEC).fieldOf("to").forGetter(ToPhaseProvider::to)
        ).apply(instance, ToPhaseProvider::new)
    );

    @Override
    public Type getType() {
        return Type.TO;
    }

    @Override
    @Nullable
    public Phase get(Context context) {
        Entity entity = context.sourceEntity();
        if (entity instanceof EnemyEntity enemy) {
            Behavior behavior = enemy.getController().getBehavior();
            if (behavior.getType() == Behavior.Type.MULTI) {
                MultiBehavior multiBehavior = (MultiBehavior) behavior;

                if (to.left().isPresent()) {
                    return multiBehavior.getPhase(to.left().get());
                } else if (to.right().isPresent()) return multiBehavior.getPhase(to.right().get().getInt(context));
            }
        }
        return null;
    }
}
