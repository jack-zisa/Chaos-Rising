package dev.creoii.chaos.entity.behavior.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public class MoveAction extends Action {
    public static final MapCodec<MoveAction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("movement").forGetter(MoveAction::getMovementId)
        ).apply(instance, MoveAction::new);
    });
    private final String movementId;

    public MoveAction(String movementId) {
        this.movementId = movementId;
    }

    @Override
    public Type getType() {
        return Type.MOVE;
    }

    public String getMovementId() {
        return movementId;
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
        Movements.MOVEMENTS.get(movementId).accept(controller.getEntity(), delta);
    }

    @Override
    public void reset(EntityController<? extends EnemyEntity> controller) {

    }
}
