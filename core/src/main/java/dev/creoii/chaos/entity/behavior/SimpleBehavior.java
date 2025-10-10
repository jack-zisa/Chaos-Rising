package dev.creoii.chaos.entity.behavior;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.entity.controller.EnemyController;

public record SimpleBehavior(Phase phase) implements Behavior {
    public static final MapCodec<SimpleBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Phase.CODEC.fieldOf("phase").forGetter(SimpleBehavior::phase)
        ).apply(instance, SimpleBehavior::new);
    });

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    @Override
    public void start(EnemyController controller, EnemyEntity entity) {
    }

    @Override
    public void update(EnemyController controller, int time, float delta) {
        phase.update(controller, time, delta);
    }
}
