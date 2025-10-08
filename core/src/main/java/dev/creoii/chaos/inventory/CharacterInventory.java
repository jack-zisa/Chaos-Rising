package dev.creoii.chaos.inventory;

import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.item.EquipmentItem;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.util.stat.ModifierEntry;

import java.util.List;

public class CharacterInventory extends Inventory {
    private CharacterEntity character;

    public CharacterInventory() {
        super(3, 4);
        getWeaponSlot().setType(Slot.Type.WEAPON);
        getAbilitySlot().setType(Slot.Type.ABILITY);
        getArmorSlot().setType(Slot.Type.ARMOR);
        getAccessorySlot().setType(Slot.Type.ACCESSORY);
    }

    public CharacterInventory withCharacter(CharacterEntity character) {
        this.character = character;
        return this;
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

    public Slot[] getHotbar() {
        return getSlots()[getSlots().length - 1];
    }

    public Slot getWeaponSlot() {
        return getHotbar()[0];
    }

    public Slot getAbilitySlot() {
        return getHotbar()[1];
    }

    public Slot getArmorSlot() {
        return getHotbar()[2];
    }

    public Slot getAccessorySlot() {
        return getHotbar()[3];
    }
}
