package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.controller.EnemyController;

public abstract class Action {
    public static Action parse(JsonValue jsonValue) {
        String actionTypeId = jsonValue.getString("id");
        return switch (actionTypeId) {
            case "movement" -> new MovementAction(jsonValue.getString("movement"), jsonValue);
            default -> throw new IllegalStateException("Unexpected value: " + actionTypeId);
        };
    }

    public abstract void update(EnemyController controller, int time, float delta);
}
