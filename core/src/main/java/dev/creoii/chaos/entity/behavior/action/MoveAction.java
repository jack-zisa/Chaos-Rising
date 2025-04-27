package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.controller.EntityController;

public class MoveAction extends Action {
    private final String movementId;

    public MoveAction(String movementId, JsonValue data) {
        super(data);
        this.movementId = movementId;
    }

    @Override
    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
        Movements.MOVEMENTS.get(movementId).accept(controller.getEntity(), delta, getData());
    }

    @Override
    public void reset(EntityController<? extends EnemyEntity> controller) {

    }
}
