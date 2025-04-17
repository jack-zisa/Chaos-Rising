package dev.creoii.chaos.entity.inventory;

import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.item.ItemStack;

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
        if (slot.getType() != Slot.Type.NONE && slot.getType().getItemPredicate().test(stack.getItem())) {
            character.getStats().applyModifier(stack.getItem().getStatModifier());
        }
    }

    @Override
    public void onRemoveItemFromSlot(Slot slot, ItemStack stack) {
        if (slot.getType() != Slot.Type.NONE && slot.getType().getItemPredicate().test(stack.getItem())) {
            character.getStats().removeModifier(stack.getItem().getStatModifier().uuid());
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
