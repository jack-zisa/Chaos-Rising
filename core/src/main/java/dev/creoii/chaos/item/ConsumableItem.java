package dev.creoii.chaos.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import dev.creoii.chaos.InputManager;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.util.Rarity;
import dev.creoii.chaos.util.stat.ModifierEntry;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.List;

public class ConsumableItem extends Item {
    private final List<ModifierEntry> statBonus;
    private final List<StatusEffect> statusEffects;

    public ConsumableItem(Rarity rarity, String textureId, List<ModifierEntry> statBonus, List<StatusEffect> statusEffects) {
        super(Type.CONSUMABLE, rarity, textureId);
        this.statBonus = statBonus;
        this.statusEffects = statusEffects;
    }

    public List<ModifierEntry> getStatBonus() {
        return statBonus;
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
        if (getStatBonus() != null) {
            StatContainer stats = character.getStats();
            getStatBonus().forEach(modifierEntry -> {
                switch (modifierEntry.type()) {
                    case HEALTH -> stats.setHealth(Math.min(character.getMaxStats().health.value(), stats.health.value() + modifierEntry.amount()));
                    case SPEED -> stats.setSpeed(Math.min(character.getMaxStats().speed.value(), stats.speed.value() + modifierEntry.amount()));
                    case ATTACK_SPEED -> stats.setAttackSpeed(Math.min(character.getMaxStats().attackSpeed.value(), stats.attackSpeed.value() + modifierEntry.amount()));
                    case DEFENSE -> stats.setDefense(Math.min(character.getMaxStats().defense.value(), stats.defense.value() + modifierEntry.amount()));
                    case ATTACK -> stats.setAttack(Math.min(character.getMaxStats().attack.value(), stats.attack.value() + modifierEntry.amount()));
                    case VITALITY -> stats.setVitality(Math.min(character.getMaxStats().vitality.value(), stats.vitality.value() + modifierEntry.amount()));
                }
            });
        }
        if (!getStatusEffects().isEmpty()) {
            statusEffects.forEach(statusEffect -> character.addStatusEffect(statusEffect, statusEffect.getAmplifier(), statusEffect.getDuration()));
        }
        slot.setStack(ItemStack.EMPTY);
    }
}
