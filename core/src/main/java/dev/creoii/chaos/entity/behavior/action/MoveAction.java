package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.controller.EnemyController;

public class MoveAction extends Action {
    private final String movementId;

    public MoveAction(String movementId, JsonValue data) {
        super(data);
        this.movementId = movementId;
    }

    @Override
    public void update(EnemyController controller, int time, float delta) {
        Movements.MOVEMENTS.get(movementId).accept(controller.getEntity(), delta, getData());
    }

    @Override
    public void reset(EnemyController controller) {

    }
}
