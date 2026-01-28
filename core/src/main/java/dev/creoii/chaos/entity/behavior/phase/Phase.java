package dev.creoii.chaos.entity.behavior.phase;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.behavior.transition.AfterTransition;
import dev.creoii.chaos.entity.behavior.transition.NeverTransition;
import dev.creoii.chaos.entity.controller.EntityController;
import dev.creoii.chaos.entity.behavior.action.Action;
import dev.creoii.chaos.entity.behavior.transition.Transition;
import dev.creoii.chaos.util.context.ContextProvider;

import java.util.List;

public class Phase {
    public static final Codec<Phase> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Transition.CODEC.fieldOf("transition").orElse(NeverTransition.INSTANCE).forGetter(Phase::getTransition),
            Action.CODEC.listOf().fieldOf("actions").forGetter(Phase::getActions)
        ).apply(instance, Phase::new);
    });

    private String id;
    private final Transition transition;
    private final List<Action> actions;
    private int startTime;

    public Phase(Transition transition, List<Action> actions) {
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

    public Transition getTransition() {
        return transition;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void start(EntityController<? extends EnemyEntity> controller, int startTime) {
        this.startTime = startTime;

        if (transition instanceof AfterTransition afterTransition) {
            afterTransition.setStartTime(startTime);
        }

        actions.forEach(action -> action.start(controller));
    }

    public void end(EntityController<? extends EnemyEntity> controller) {
        this.startTime = 0;
        actions.forEach(action -> action.end(controller));

        if (transition instanceof AfterTransition afterTransition) {
            afterTransition.setStartTime(0);
        }
    }

    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
        if (startTime >= 0) {
            actions.forEach(action -> action.update(controller, time, delta));
        }
    }

    public boolean shouldTransition(ContextProvider context, int time) {
        return transition.shouldTransition(context, time);
    }
}
