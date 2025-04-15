package dev.creoii.chaos.entity.inventory;

import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.item.ItemStack;

public class CharacterInventory extends Inventory {
    private final CharacterEntity character;

    public CharacterInventory(CharacterEntity character) {
        super(3, 4);
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
}
