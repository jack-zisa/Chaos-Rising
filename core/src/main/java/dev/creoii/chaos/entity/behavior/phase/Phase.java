package dev.creoii.chaos.entity.behavior.phase;

import dev.creoii.chaos.entity.behavior.transition.Transition;
import dev.creoii.chaos.entity.controller.EnemyController;

public class Phase {
    private final String id;
    private final int duration;
    private final Transition transition;
    private int startTime;

    public Phase(String id, int duration, Transition transition) {
        this.id = id;
        this.duration = duration;
        this.transition = transition;
        startTime = -1;
    }

    public String getId() {
        return id;
    }

    public void start(EnemyController controller, int startTime) {
        this.startTime = startTime;
    }

    public void end(EnemyController controller) {
        this.startTime = 0;
    }

    public void update(EnemyController controller, int time, float delta) {
        if (startTime >= 0) {
            System.out.println(id);
        }
    }

    public boolean shouldTransition(EnemyController controller, int time) {
        return (time - startTime) >= duration;
    }

    public Phase getNext(EnemyController controller) {
        return transition.getFunction().apply(controller, this, transition.getData());
    }
}
