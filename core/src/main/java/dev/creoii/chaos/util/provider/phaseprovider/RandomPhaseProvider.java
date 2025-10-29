package dev.creoii.chaos.util.provider.phaseprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.entity.behavior.MultiBehavior;
import dev.creoii.chaos.entity.behavior.phase.Phase;

import javax.annotation.Nullable;

public record RandomPhaseProvider() implements PhaseProvider {
    private static final RandomPhaseProvider INSTANCE = new RandomPhaseProvider();
    public static final MapCodec<RandomPhaseProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.RANDOM;
    }

    @Override
    @Nullable
    public Phase get(Context context) {
        Entity entity = context.sourceEntity();
        if (entity instanceof EnemyEntity enemy) {
            Behavior behavior = enemy.getController().getBehavior();
            if (behavior.getType() == Behavior.Type.MULTI) {
                MultiBehavior multiBehavior = (MultiBehavior) behavior;
                return multiBehavior.getPhase(context.random().nextInt(multiBehavior.getPhaseCount()));
            }
        }
        return null;
    }
}
