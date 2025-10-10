package dev.creoii.chaos.effect;

import java.util.HashMap;
import java.util.Map;

public class StatusEffect {
    private final StatusEffectType type;
    private final int amplifier;
    private final Map<String, Object> data;
    private int duration;

    public StatusEffect(StatusEffectType type, int amplifier, int duration) {
        this.type = type;
        this.amplifier = amplifier;
        this.duration = duration;
        data = new HashMap<>();
    }

    public StatusEffectType getType() {
        return type;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public int getDuration() {
        return duration;
    }

    public void decrementDuration() {
        --duration;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
