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

import java.util.ArrayList;
import java.util.List;

public class Phase {
    public static final Codec<Phase> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Transition.CODEC.fieldOf("transition").orElse(NeverTransition.INSTANCE).forGetter(Phase::getTransition),
            Action.CODEC.listOf().fieldOf("actions").forGetter(phase -> {
                List<Action> list = new ArrayList<>();
                list.addAll(phase.instantActions);
                list.addAll(phase.updateActions);
                return list;
            })
        ).apply(instance, Phase::new);
    });

    private String id;
    private final Transition transition;
    private final List<Action> instantActions;
    private final List<Action> updateActions;
    private int startTime;

    public Phase(Transition transition, List<Action> actions) {
        this.transition = transition;
        this.instantActions = actions.stream().filter(Action::isInstant).toList();
        this.updateActions = actions.stream().filter(action -> !action.isInstant()).toList();
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

    public List<Action> getInstantActions() {
        return instantActions;
    }

    public List<Action> getUpdateActions() {
        return updateActions;
    }

    public void start(EntityController<? extends EnemyEntity> controller, int startTime) {
        this.startTime = startTime;

        if (transition instanceof AfterTransition afterTransition) {
            afterTransition.setStartTime(startTime);
        }

        instantActions.forEach(action -> action.start(controller));
        updateActions.forEach(action -> action.start(controller));
    }

    public void update(EntityController<? extends EnemyEntity> controller, int time, float delta) {
        if (startTime >= 0) {
            updateActions.forEach(action -> action.update(controller, time, delta));
        }
    }

    public void end(EntityController<? extends EnemyEntity> controller) {
        this.startTime = 0;
        updateActions.forEach(action -> action.end(controller));

        if (transition instanceof AfterTransition afterTransition) {
            afterTransition.setStartTime(0);
        }
    }

    public boolean shouldTransition(ContextProvider context, int time) {
        return transition.shouldTransition(context, time);
    }
}
