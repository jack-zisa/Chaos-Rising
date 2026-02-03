package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public class KillAction extends InstantAction {
    public static final KillAction INSTANCE = new KillAction();
    public static final MapCodec<KillAction> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.KILL;
    }

    @Override
    public void start(EntityController<? extends EnemyEntity> controller) {
        controller.getEntity().remove();
    }
}
