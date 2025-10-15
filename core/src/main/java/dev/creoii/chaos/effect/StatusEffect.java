package dev.creoii.chaos.effect;

public class StatusEffect {
    private final StatusEffectType type;
    private final int amplifier;
    private int duration;

    public StatusEffect(StatusEffectType type, int amplifier, int duration) {
        this.type = type;
        this.amplifier = amplifier;
        this.duration = duration;
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
}
