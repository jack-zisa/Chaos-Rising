package dev.creoii.chaos.entity.controller;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.ai.phase.Phase;
import dev.creoii.chaos.entity.ai.phase.PhaseKey;

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
        this(null, phases, startPhaseKey);
    }

    public EnemyController(EnemyEntity entity, Map<PhaseKey, Phase> phases, String startPhaseKey) {
        super(entity);
        this.startPhaseKey = startPhaseKey;
        phaseKeys = HashBiMap.create();
        phases.keySet().forEach(key -> phaseKeys.put(key.name(), key.index()));
        this.phases = phases.values().toArray(new Phase[0]);
        random = new Random();
    }

    public Phase getPhase(int index) {
        return phases[index];
    }

    public Phase getPhase(String id) {
        return phases[phaseKeys.get(id)];
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

    public Phase[] getPhases() {
        return phases;
    }

    public BiMap<String, Integer> getPhaseKeys() {
        return phaseKeys;
    }

    public String getStartPhaseKey() {
        return startPhaseKey;
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
