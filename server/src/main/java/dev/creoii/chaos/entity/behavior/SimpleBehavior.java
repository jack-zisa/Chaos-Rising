package dev.creoii.chaos.entity.behavior;

import dev.creoii.chaos.entity.ServerEnemyEntity;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.entity.controller.EnemyController;

public record SimpleBehavior(Phase phase) implements Behavior {
    @Override
    public void start(EnemyController controller, ServerEnemyEntity entity) {
    }

    @Override
    public void update(EnemyController controller, int time, float delta) {
        phase.update(controller, time, delta);
    }
}
