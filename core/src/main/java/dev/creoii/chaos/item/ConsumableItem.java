package dev.creoii.chaos.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.List;

public class ConsumableItem extends Item {
    private final StatContainer statContainer;
    private final List<StatusEffect> statusEffects;

    public ConsumableItem(Rarity rarity, String textureId, StatContainer statContainer, List<StatusEffect> statusEffects) {
        super(Type.CONSUMABLE, rarity, textureId);
        this.statContainer = statContainer;
        this.statusEffects = statusEffects;
    }

    public StatContainer getStatContainer() {
        return statContainer;
    }

    public List<StatusEffect> getStatusEffects() {
        return statusEffects;
    }

    @Override
    public boolean clickInSlot(InputManager manager, Slot slot, ItemStack stack) {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            consume(manager, slot, stack);
            return true;
        }
        return super.clickInSlot(manager, slot, stack);
    }

    public void consume(InputManager manager, Slot slot, ItemStack stack) {
        CharacterEntity character = manager.getMain().getGame().getActiveCharacter();
        if (getStatContainer() != null) {
            StatContainer characterStats = character.getStats();
            characterStats.setHealth(Math.min(character.getMaxStats().health.value(), characterStats.health.base() + statContainer.health.base()));
        }
        if (!getStatusEffects().isEmpty()) {
            statusEffects.forEach(statusEffect -> character.addStatusEffect(statusEffect, statusEffect.getAmplifier(), statusEffect.getDuration()));
        }
        slot.setStack(ItemStack.EMPTY);
    }
}
