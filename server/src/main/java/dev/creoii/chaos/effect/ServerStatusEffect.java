package dev.creoii.chaos.effect;

import java.util.function.BiConsumer;

public class ServerStatusEffect extends StatusEffect {
    private final BiConsumer<ServerLivingEntity, ServerStatusEffect> starter;
    private final BiConsumer<ServerLivingEntity, ServerStatusEffect> applier;
    private final BiConsumer<ServerLivingEntity, ServerStatusEffect> remover;

    public ServerStatusEffect(String id, BiConsumer<ServerLivingEntity, ServerStatusEffect> starter, BiConsumer<ServerLivingEntity, ServerStatusEffect> applier, BiConsumer<ServerLivingEntity, ServerStatusEffect> remover) {
        super(id);
        this.starter = starter;
        this.applier = applier;
        this.remover = remover;
    }


    public BiConsumer<ServerLivingEntity, ServerStatusEffect> getStarter() {
        return starter;
    }

    public BiConsumer<ServerLivingEntity, ServerStatusEffect> getApplier() {
        return applier;
    }

    public BiConsumer<ServerLivingEntity, ServerStatusEffect> getRemover() {
        return remover;
    }
}
