package dev.creoii.chaos.entity.controller;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.entity.behavior.phase.PhaseKey;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class EnemyController extends EntityController<EnemyEntity> {
    private final Phase[] phases;
    private final BiMap<String, Integer> phaseKeys;
    private final String startPhaseKey;
    private final Random random;
    private Phase currentPhase;
    private int time;

    public EnemyController(Map<PhaseKey, Phase> phases, String startPhaseKey) {
        super(null);
        this.startPhaseKey = startPhaseKey;
        phaseKeys = HashBiMap.create();
        phases.keySet().forEach(key -> phaseKeys.put(key.name(), key.index()));
        this.phases = phases.values().toArray(new Phase[0]);
        random = new Random();
    }

    public EnemyController(EnemyController controller) {
        super(null);
        this.startPhaseKey = controller.getStartPhaseKey();
        phaseKeys = controller.phaseKeys;
        phases = Arrays.copyOf(controller.phases, controller.phases.length);
        random = new Random();
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

    public EnemyEntity getEntity() {
        return entity;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public Random getRandom() {
        return random;
    }

    public void start(EnemyEntity entity) {
        this.entity = entity;
        time = entity.getGame().getGametime();
        currentPhase = getPhase(startPhaseKey);
        currentPhase.start(this, time);
    }

    @Override
    public void control(int gametime, float delta) {
        if (entity != null) {
            currentPhase.update(this, ++time, delta);

            if (currentPhase.shouldTransition(this, time)) {
                currentPhase.end(this);
                currentPhase = currentPhase.getNext(this);
                currentPhase.start(this, time);
            }
        }
    }
}
