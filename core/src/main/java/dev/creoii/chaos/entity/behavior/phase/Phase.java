package dev.creoii.chaos.entity.behavior.phase;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.behavior.transition.Transitions;
import dev.creoii.chaos.entity.controller.EnemyController;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.behavior.MultiBehavior;
import dev.creoii.chaos.entity.behavior.action.Action;
import dev.creoii.chaos.entity.behavior.transition.Transition;

import java.util.List;

public class Phase {
    public static final Codec<Phase> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("duration").orElse(-1).forGetter(Phase::getDuration),
            Action.CODEC.listOf().fieldOf("actions").forGetter(Phase::getActions)
        ).apply(instance, (duration, actions) -> new Phase(duration, Transitions.ALL.get(Transitions.Key.NEXT.ordinal()), actions));
    });

    private String id;
    private final int duration;
    private final Transition transition;
    private final List<Action> actions;
    private int startTime;

    public Phase(int duration, Transition transition, List<Action> actions) {
        this.duration = duration;
        this.transition = transition;
        this.actions = actions;
        startTime = -1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void start(EntityController<? extends EnemyEntity> controller, int startTime) {
        this.startTime = startTime;
    }

    public void end(EntityController<? extends EnemyEntity> controller) {
        this.startTime = 0;
        actions.forEach(action -> action.reset(controller));
    }

    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
        if (startTime >= 0 || transition == null) {
            actions.forEach(action -> action.update(controller, time, delta));
        }
    }

    public boolean shouldTransition(int time) {
        if (duration == -1 || transition == null)
            return false;
        return (time - startTime) >= duration;
    }

    public Phase getNext(EnemyController controller) {
        return transition.getFunction().apply((MultiBehavior) controller.getBehavior(), this, transition.getData());
    }
}
