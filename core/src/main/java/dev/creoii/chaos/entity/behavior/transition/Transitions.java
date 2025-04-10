package dev.creoii.chaos.entity.behavior.transition;

import java.util.HashMap;
import java.util.Map;

public class Transitions {
    public static final Map<String, Transition> ALL = new HashMap<>();

    static {
        Transition.register("next", (controller, phase, data) -> {
            int index = (controller.getIndex(phase) + 1) % controller.getPhaseCount();
            return controller.getPhase(index);
        });

        Transition.register("random", (controller, phase, data) -> controller.getPhase(controller.getRandom().nextInt(controller.getPhaseCount())));

        Transition.register("to", (controller, phase, data) -> controller.getPhase(data.getInt("to")));
    }
}
