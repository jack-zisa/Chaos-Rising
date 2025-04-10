package dev.creoii.chaos.entity.behavior.transition;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.entity.controller.EnemyController;
import dev.creoii.chaos.util.function.TriFunction;

import javax.annotation.Nullable;

public class Transition {
    private final TriFunction<EnemyController, Phase, JsonValue, Phase> function;
    private JsonValue data;

    public Transition(TriFunction<EnemyController, Phase, JsonValue, Phase> function) {
        this.function = function;
    }

    public TriFunction<EnemyController, Phase, JsonValue, Phase> getFunction() {
        return function;
    }

    public JsonValue getData() {
        return data;
    }

    public void setData(JsonValue data) {
        this.data = data;
    }

    static void register(String id, TriFunction<EnemyController, Phase, JsonValue, Phase> function) {
        Transitions.ALL.put(id, new Transition(function));
    }

    @Nullable
    public static Transition parse(JsonValue jsonValue) {
        if (jsonValue == null)
            return null;
        Transition transition = Transitions.ALL.get(jsonValue.getString("id"));
        transition.setData(jsonValue);
        return transition;
    }
}
