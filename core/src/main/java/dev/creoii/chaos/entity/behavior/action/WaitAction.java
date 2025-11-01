package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public class WaitAction extends Action {
    public static final WaitAction INSTANCE = new WaitAction();
    public static final MapCodec<WaitAction> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.WAIT;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {

    }

    @Override
    public void reset(EntityController<? extends EnemyEntity> controller) {

    }
}
