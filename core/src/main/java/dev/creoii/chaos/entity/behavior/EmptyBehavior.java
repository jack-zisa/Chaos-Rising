package dev.creoii.chaos.entity.behavior;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EnemyController;

public record EmptyBehavior() implements Behavior {
    public static final EmptyBehavior INSTANCE = new EmptyBehavior();
    public static final MapCodec<EmptyBehavior> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.EMPTY;
    }

    @Override
    public void start(EnemyController controller, EnemyEntity entity) {
    }

    @Override
    public void update(EnemyController controller, int time, float delta) {
    }

    @Override
    public Behavior copy() {
        return INSTANCE;
    }
}
