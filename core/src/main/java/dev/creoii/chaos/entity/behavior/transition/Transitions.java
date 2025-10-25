package dev.creoii.chaos.entity.behavior.transition;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.behavior.MultiBehavior;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class Transitions {
    public static final Int2ObjectOpenHashMap<Transition> ALL = new Int2ObjectOpenHashMap<>();

    public enum Key {
        NEXT,
        PREVIOUS,
        RANDOM,
        TO
    }

    static {
        Transition.register(Key.NEXT, (behavior, phase, data) -> behavior.getPhase((behavior.getIndex(phase) + 1) % behavior.getPhaseCount()));
        Transition.register(Key.PREVIOUS, (behavior, phase, data) -> behavior.getPhase((behavior.getIndex(phase) + - 1) % behavior.getPhaseCount()));
        Transition.register(Key.RANDOM, (behavior, phase, data) -> behavior.getPhase(MultiBehavior.RANDOM.nextInt(behavior.getPhaseCount())));
        Transition.register(Key.TO, (behavior, phase, data) -> {
            JsonValue toValue = data.get("to");
            if (toValue.isNumber()) {
                return behavior.getPhase(data.getInt("to"));
            }
            return behavior.getPhase(data.getString("to"));
        });
    }
}
