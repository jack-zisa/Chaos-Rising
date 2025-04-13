package dev.creoii.chaos.entity.behavior.phase;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.entity.behavior.MultiBehavior;
import dev.creoii.chaos.entity.behavior.action.Action;
import dev.creoii.chaos.entity.behavior.transition.Transition;
import dev.creoii.chaos.entity.controller.EnemyController;

import java.util.ArrayList;
import java.util.List;

public class Phase {
    private final String id;
    private final int duration;
    private final Transition transition;
    private final List<Action> actions;
    private int startTime;

    public Phase(String id, int duration, Transition transition, List<Action> actions) {
        this.id = id;
        this.duration = duration;
        this.transition = transition;
        this.actions = actions;
        startTime = -1;
    }

    public static Phase parse(JsonValue jsonValue) {
        JsonValue actions = jsonValue.get("actions");
        List<Action> parsedActions = new ArrayList<>();
        for (JsonValue actionJson : actions) {
            parsedActions.add(Action.parse(actionJson));
        }
        return new Phase(jsonValue.name, jsonValue.getInt("duration", 0), Transition.parse(jsonValue.get("transition")), parsedActions);
    }

    public String getId() {
        return id;
    }

    public void start(EnemyController controller, int startTime) {
        this.startTime = startTime;
    }

    public void end(EnemyController controller) {
        this.startTime = 0;
        actions.forEach(action -> action.reset(controller));
    }

    public void update(EnemyController controller, int time, float delta) {
        if (startTime >= 0 || transition == null) {
            actions.forEach(action -> action.update(controller, time, delta));
        }
    }

    public boolean shouldTransition(EnemyController controller, int time) {
        if (transition == null)
            return false;
        return (time - startTime) >= duration;
    }

    public Phase getNext(EnemyController controller) {
        return transition.getFunction().apply((MultiBehavior) controller.getBehavior(), this, transition.getData());
    }
}
