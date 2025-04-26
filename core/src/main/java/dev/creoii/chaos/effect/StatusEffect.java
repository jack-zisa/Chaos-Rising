package dev.creoii.chaos.effect;

import dev.creoii.chaos.util.Identifiable;

import java.util.HashMap;
import java.util.Map;

public class StatusEffect implements Identifiable {
    private final String id;
    private int amplifier;
    private int duration;
    private Map<String, Object> data;

    public StatusEffect(String id) {
        this.id = id;
    }

    public void init(int amplifier, int duration) {
        this.amplifier = amplifier;
        this.duration = duration;
        data = new HashMap<>();
    }

    @Override
    public String id() {
        return id;
    }

    public Map<String, Object> getData() {
        return data;
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
}
