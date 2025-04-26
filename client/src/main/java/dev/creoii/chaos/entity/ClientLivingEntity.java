package dev.creoii.chaos.entity;

import dev.creoii.chaos.ClientGame;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientLivingEntity extends ClientEntity {
    private final StatContainer statContainer;
    private final StatContainer maxStatContainer;
    private final List<StatusEffect> statusEffects;

    public ClientLivingEntity(ClientGame game, UUID uuid, String textureId, float x, float y, float scale) {
        super(game, uuid, textureId, x, y, scale);
        statContainer = new StatContainer();
        maxStatContainer = new StatContainer();
        statusEffects = new ArrayList<>();
    }

    public StatContainer getStats() {
        return statContainer;
    }

    public StatContainer getMaxStats() {
        return maxStatContainer;
    }

    public void addStatusEffect(StatusEffect statusEffect) {
        statusEffects.add(statusEffect);
    }

    public void removeStatusEffect(StatusEffect statusEffect) {
        statusEffects.remove(statusEffect);
    }

    public void clearStatusEffects() {
        statusEffects.clear();
    }

    public boolean hasStatusEffect(String id) {
        return statusEffects.stream().anyMatch(statusEffect1 -> statusEffect1.id().equals(id));
    }
}
