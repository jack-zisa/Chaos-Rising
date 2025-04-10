package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.controller.EnemyController;

public class MovementAction extends Action {
    private final String movementId;
    private final JsonValue data;

    public MovementAction(String movementId, JsonValue data) {
        this.movementId = movementId;
        this.data = data;
    }

    @Override
    public void update(EnemyController controller, int time, float delta) {
        Movements.MOVEMENTS.get(movementId).accept(controller.getEntity(), delta, data);
    }
}
