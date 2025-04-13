package dev.creoii.chaos.entity.behavior;

import com.badlogic.gdx.utils.JsonValue;
import com.google.common.collect.BiMap;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.entity.behavior.phase.PhaseKey;
import dev.creoii.chaos.entity.controller.EnemyController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public interface Behavior {
    void start(EnemyController controller, EnemyEntity entity);

    void update(EnemyController controller, int time, float delta);

    static Behavior copy(Behavior behavior) {
        if (behavior instanceof MultiBehavior multiBehavior) {
            BiMap<Integer, String> phaseKeys = multiBehavior.getPhaseKeys().inverse();
            Map<PhaseKey, Phase> phases = new HashMap<>();
            for (int i = 0; i < multiBehavior.getPhases().length; ++i) {
                phases.put(new PhaseKey(phaseKeys.get(i), i), multiBehavior.getPhases()[i]);
            }
            return new MultiBehavior(phases, multiBehavior.getStartPhaseKey());
        } else {
            return new SimpleBehavior(((SimpleBehavior) behavior).phase());
        }
    }

    static Behavior parse(JsonValue jsonValue) {
        if (jsonValue.has("start_phase")) {
            String startPhaseKey = jsonValue.getString("start_phase");

            Map<PhaseKey, Phase> phases = new LinkedHashMap<>();
            int i = 0;
            for (JsonValue phaseValue : jsonValue.get("phases")) {
                phases.put(new PhaseKey(phaseValue.name, i), Phase.parse(phaseValue));
                ++i;
            }
            return new MultiBehavior(phases, startPhaseKey);
        } else {
            return new SimpleBehavior(Phase.parse(jsonValue.get("phase")));
        }
    }
}
