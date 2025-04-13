package dev.creoii.chaos.effect;

import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class StatusEffect implements DataManager.Identifiable {
    private String id;
    private final BiConsumer<LivingEntity, StatusEffect> starter;
    private final BiConsumer<LivingEntity, StatusEffect> applier;
    private final BiConsumer<LivingEntity, StatusEffect> remover;
    private Map<String, Object> data;
    private int amplifier;
    private int duration;

    public StatusEffect(String id, BiConsumer<LivingEntity, StatusEffect> starter, BiConsumer<LivingEntity, StatusEffect> applier, BiConsumer<LivingEntity, StatusEffect> remover) {
        this.id = id;
        this.starter = starter;
        this.applier = applier;
        this.remover = remover;
    }

    public StatusEffect(String id) {
        this(id, null, null, null);
    }

    static void register(String id, BiConsumer<LivingEntity, StatusEffect> starter, BiConsumer<LivingEntity, StatusEffect> applier, BiConsumer<LivingEntity, StatusEffect> remover) {
        StatusEffects.ALL.put(id, new StatusEffect(id, starter, applier, remover));
    }

    static void register(String id) {
        StatusEffects.ALL.put(id, new StatusEffect(id));
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

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public BiConsumer<LivingEntity, StatusEffect> getStarter() {
        return starter;
    }

    public BiConsumer<LivingEntity, StatusEffect> getApplier() {
        return applier;
    }

    public BiConsumer<LivingEntity, StatusEffect> getRemover() {
        return remover;
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
