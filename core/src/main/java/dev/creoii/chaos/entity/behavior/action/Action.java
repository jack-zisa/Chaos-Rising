package dev.creoii.chaos.entity.behavior.action;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.attack.Attack;
import dev.creoii.chaos.entity.controller.EnemyController;

public abstract class Action {
    private final JsonValue data;

    protected Action(JsonValue data) {
        this.data = data;
    }

    public JsonValue getData() {
        return data;
    }

    public static Action parse(JsonValue jsonValue) {
        String actionTypeId = jsonValue.getString("id");
        return switch (actionTypeId) {
            case "movement" -> new MovementAction(jsonValue.getString("movement"), jsonValue);
            case "attack" -> new AttackAction(Attack.parse(jsonValue.get("attack")), jsonValue);
            default -> throw new IllegalStateException("Unexpected value: " + actionTypeId);
        };
    }

    public abstract void update(EnemyController controller, int time, float delta);

    public abstract void reset(EnemyController controller);
}
