package dev.creoii.chaos.entity.behavior;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.behavior.action.Action;
import dev.creoii.chaos.entity.controller.EnemyController;

import java.util.ArrayList;
import java.util.List;

public record SimpleBehavior(List<Action> actions) implements Behavior {
    public static final MapCodec<SimpleBehavior> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Action.CODEC.listOf().fieldOf("actions").forGetter(SimpleBehavior::actions)
        ).apply(instance, SimpleBehavior::new);
    });

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    @Override
    public void start(EnemyController controller, EnemyEntity entity) {
        actions.forEach(action -> action.start(controller));
    }

    @Override
    public void update(EnemyController controller, int time, float delta) {
        actions.forEach(action -> action.update(controller, time, delta));
    }

    @Override
    public Behavior copy() {
        return new SimpleBehavior(new ArrayList<>(actions));
    }
}
