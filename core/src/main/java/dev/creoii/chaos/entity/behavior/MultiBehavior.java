package dev.creoii.chaos.entity.behavior;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.entity.behavior.phase.PhaseKey;
import dev.creoii.chaos.entity.controller.EnemyController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MultiBehavior implements Behavior {
    public static final Random RANDOM = new Random();
    private final Phase[] phases;
    private final BiMap<String, Integer> phaseKeys;
    private final String startPhaseKey;
    private Phase currentPhase;

    public MultiBehavior(Map<PhaseKey, Phase> phases, String startPhaseKey) {
        this.startPhaseKey = startPhaseKey;
        phaseKeys = HashBiMap.create();
        phases.keySet().forEach(key -> phaseKeys.put(key.name(), key.index()));
        this.phases = phases.values().toArray(new Phase[0]);
    }

    public BiMap<String, Integer> getPhaseKeys() {
        return phaseKeys;
    }

    public Phase getPhase(int index) {
        return phases[index];
    }

    public Phase getPhase(String id) {
        return phases[phaseKeys.get(id)];
    }

    public Phase[] getPhases() {
        return phases;
    }

    public String getStartPhaseKey() {
        return startPhaseKey;
    }

    public int getIndex(Phase phase) {
        return phaseKeys.get(phase.getId());
    }

    public int getPhaseCount() {
        return phases.length;
    }

    @Override
    public void start(EnemyController controller, EnemyEntity entity) {
        currentPhase = getPhase(startPhaseKey);
        currentPhase.start(controller, controller.getTime());
    }

    @Override
    public void update(EnemyController controller, int time, float delta) {
        currentPhase.update(controller, time, delta);

        if (currentPhase.shouldTransition(controller, time)) {
            currentPhase.end(controller);
            currentPhase = currentPhase.getNext(controller);
            currentPhase.start(controller, time);
        }
    }
}
