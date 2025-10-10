package dev.creoii.chaos.entity.behavior;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Codec;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.behavior.phase.Phase;
import dev.creoii.chaos.entity.behavior.phase.PhaseKey;
import dev.creoii.chaos.entity.controller.EnemyController;

import java.util.HashMap;
import java.util.Map;

public interface Behavior {
    Codec<Behavior> CODEC = Behavior.Type.CODEC.dispatch(Behavior::getType, type -> switch (type) {
        case SIMPLE -> SimpleBehavior.CODEC;
        case MULTI -> MultiBehavior.CODEC;
        case EMPTY -> EmptyBehavior.CODEC;
    });

    Type getType();

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

    enum Type {
        SIMPLE,
        MULTI,
        EMPTY;

        public static final Codec<Type> CODEC = Codec.STRING.xmap(s -> Type.valueOf(s.toUpperCase()), type -> type.name().toLowerCase());
    }
}
