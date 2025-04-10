package dev.creoii.chaos.entity.behavior.transition;

import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class Transitions {
    public static final Map<String, Transition> ALL = new HashMap<>();

    static {
        Transition.register("next", (controller, phase, data) -> controller.getPhase((controller.getIndex(phase) + 1) % controller.getPhaseCount()));
        Transition.register("previous", (controller, phase, data) -> controller.getPhase((controller.getIndex(phase) + - 1) % controller.getPhaseCount()));
        Transition.register("random", (controller, phase, data) -> controller.getPhase(controller.getRandom().nextInt(controller.getPhaseCount())));
        Transition.register("to", (controller, phase, data) -> {
            JsonValue toValue = data.get("to");
            if (toValue.isNumber()) {
                return controller.getPhase(data.getInt("to"));
            }
            return controller.getPhase(data.getString("to"));
        });
    }
}
