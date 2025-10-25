package dev.creoii.chaos.entity.behavior.transition;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.behavior.MultiBehavior;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.util.function.TriFunction;

public class Transition {
    private final TriFunction<MultiBehavior, Phase, JsonValue, Phase> function;
    private JsonValue data;

    public Transition(TriFunction<MultiBehavior, Phase, JsonValue, Phase> function) {
        this.function = function;
    }

    public TriFunction<MultiBehavior, Phase, JsonValue, Phase> getFunction() {
        return function;
    }

    public JsonValue getData() {
        return data;
    }

    public void setData(JsonValue data) {
        this.data = data;
    }

    static void register(Transitions.Key key, TriFunction<MultiBehavior, Phase, JsonValue, Phase> function) {
        Transitions.ALL.put(key.ordinal(), new Transition(function));
    }
}
