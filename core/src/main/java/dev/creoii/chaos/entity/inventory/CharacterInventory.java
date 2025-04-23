package dev.creoii.chaos.entity.inventory;

import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.item.EquipmentItem;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.util.stat.ModifierEntry;

import java.util.List;

public class CharacterInventory extends Inventory {
    private final CharacterEntity character;

    public CharacterInventory(CharacterEntity character) {
        super(3, 4);
        slots[slots.length - 1][0].setType(Slot.Type.WEAPON);
        slots[slots.length - 1][1].setType(Slot.Type.ABILITY);
        slots[slots.length - 1][2].setType(Slot.Type.ARMOR);
        slots[slots.length - 1][3].setType(Slot.Type.ACCESSORY);
        this.character = character;
    }

    public CharacterEntity getCharacter() {
        return character;
    }

    @Override
    public void onAddItemToSlot(Slot slot, ItemStack stack) {
        if (slot.getType() != Slot.Type.NONE && slot.getType().getItemPredicate().test(stack.getItem()) && stack.getItem() instanceof EquipmentItem equipmentItem) {
            List<ModifierEntry> statBonus = equipmentItem.getStatBonus();
            statBonus.forEach(modifierEntry -> {
                switch (modifierEntry.modifierType()) {
                    case BASE -> modifierEntry.apply(character.getStats());
                    case MAX -> modifierEntry.apply(character.getMaxStats());
                    case ALL -> {
                        modifierEntry.apply(character.getStats());
                        modifierEntry.apply(character.getMaxStats());
                    }
                }
            });
        }
    }

    @Override
    public void onRemoveItemFromSlot(Slot slot, ItemStack stack) {
        if (slot.getType() != Slot.Type.NONE && slot.getType().getItemPredicate().test(stack.getItem()) && stack.getItem() instanceof EquipmentItem equipmentItem) {
            List<ModifierEntry> statBonus = equipmentItem.getStatBonus();
            statBonus.forEach(modifierEntry -> {
                switch (modifierEntry.modifierType()) {
                    case BASE -> modifierEntry.remove(character.getStats());
                    case MAX -> modifierEntry.remove(character.getMaxStats());
                    case ALL -> {
                        modifierEntry.remove(character.getStats());
                        modifierEntry.remove(character.getMaxStats());
                    }
                }
            });
        }
    }

    public Slot getWeaponSlot() {
        return slots[slots.length - 1][0];
    }

    public Slot getAbilitySlot() {
        return slots[slots.length - 1][1];
    }

    public Slot getArmorSlot() {
        return slots[slots.length - 1][2];
    }

    public Slot getAccessorySlot() {
        return slots[slots.length - 1][3];
    }
}
