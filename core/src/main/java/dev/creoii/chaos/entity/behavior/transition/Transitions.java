package dev.creoii.chaos.entity.behavior.transition;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.behavior.Behavior;
import dev.creoii.chaos.entity.behavior.MultiBehavior;

import java.util.HashMap;
import java.util.Map;

public class Transitions {
    public static final Map<String, Transition> ALL = new HashMap<>();

    static {
        Transition.register("next", (behavior, phase, data) -> behavior.getPhase((behavior.getIndex(phase) + 1) % behavior.getPhaseCount()));
        Transition.register("previous", (behavior, phase, data) -> behavior.getPhase((behavior.getIndex(phase) + - 1) % behavior.getPhaseCount()));
        Transition.register("random", (behavior, phase, data) -> behavior.getPhase(MultiBehavior.RANDOM.nextInt(behavior.getPhaseCount())));
        Transition.register("to", (behavior, phase, data) -> {
            JsonValue toValue = data.get("to");
            if (toValue.isNumber()) {
                return behavior.getPhase(data.getInt("to"));
            }
            return behavior.getPhase(data.getString("to"));
        });
    }
}
