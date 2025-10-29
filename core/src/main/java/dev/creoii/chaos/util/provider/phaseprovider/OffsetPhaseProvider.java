package dev.creoii.chaos.util.provider.phaseprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.entity.behavior.MultiBehavior;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

import javax.annotation.Nullable;

public record OffsetPhaseProvider(NumberProvider offset) implements PhaseProvider {
    private static final OffsetPhaseProvider NEXT = new OffsetPhaseProvider(ConstantNumberProvider.ONE);
    private static final OffsetPhaseProvider PREV = new OffsetPhaseProvider(ConstantNumberProvider.NEG_ONE);
    public static final MapCodec<OffsetPhaseProvider> NEXT_CODEC = MapCodec.unit(NEXT);
    public static final MapCodec<OffsetPhaseProvider> PREV_CODEC = MapCodec.unit(PREV);
    public static final MapCodec<OffsetPhaseProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            NumberProvider.CODEC.fieldOf("offset").forGetter(OffsetPhaseProvider::offset)
        ).apply(instance, OffsetPhaseProvider::new);
    });

    @Override
    public Type getType() {
        return Type.OFFSET;
    }

    @Override
    @Nullable
    public Phase get(Context context) {
        Entity entity = context.sourceEntity();
        if (entity instanceof EnemyEntity enemy) {
            Behavior behavior = enemy.getController().getBehavior();
            if (behavior.getType() == Behavior.Type.MULTI) {
                MultiBehavior multiBehavior = (MultiBehavior) behavior;
                return multiBehavior.getPhase((multiBehavior.getIndex(multiBehavior.getCurrentPhase()) + offset.getInt(context)) % multiBehavior.getPhaseCount());
            }
        }
        return null;
    }
}
