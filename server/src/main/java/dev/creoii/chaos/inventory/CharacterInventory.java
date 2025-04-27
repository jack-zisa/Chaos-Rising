package dev.creoii.chaos.inventory;

import dev.creoii.chaos.entity.character.ServerCharacterEntity;
import dev.creoii.chaos.item.EquipmentItem;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.packet.c2s.SlotUpdateC2S;
import dev.creoii.chaos.util.stat.ModifierEntry;

import java.util.List;

public class CharacterInventory extends Inventory {
    private final ServerCharacterEntity character;

    public CharacterInventory(ServerCharacterEntity character) {
        super(3, 4);
        getWeaponSlot().setType(Slot.Type.WEAPON);
        getAbilitySlot().setType(Slot.Type.ABILITY);
        getArmorSlot().setType(Slot.Type.ARMOR);
        getAccessorySlot().setType(Slot.Type.ACCESSORY);
        this.character = character;
    }

    public ServerCharacterEntity getCharacter() {
        return character;
    }

    public void updateSlot(SlotUpdateC2S.Action action, Inventory from, Inventory to, Slot fromSlot, Slot toSlot) {
        if (action == SlotUpdateC2S.Action.SWAP) {
            from.onRemoveItemFromSlot(fromSlot, fromSlot.getStack());
            to.onRemoveItemFromSlot(toSlot, toSlot.getStack());
            ItemStack takeTouched = toSlot.takeStack();
            toSlot.setStack(fromSlot.getStack().copy());
            from.onAddItemToSlot(toSlot, toSlot.getStack());
            fromSlot.setStack(takeTouched);
            to.onAddItemToSlot(fromSlot, takeTouched);
        } else if (action == SlotUpdateC2S.Action.MOVE) {
            from.onRemoveItemFromSlot(fromSlot, fromSlot.getStack());
            toSlot.setStack(fromSlot.getStack().copy());
            to.onAddItemToSlot(toSlot, toSlot.getStack());
        } else if (action == SlotUpdateC2S.Action.QUICK_MOVE) {
            from.onRemoveItemFromSlot(toSlot, toSlot.getStack());
            to.addItem(toSlot.takeStack());
        }
    }

    @Override
    public void onAddItemToSlot(Slot slot, ItemStack stack) {
        if (slot.getType() != Slot.Type.NONE && slot.getType().getItemPredicate().test(stack.getItem()) && stack.getItem() instanceof EquipmentItem equipmentItem) {
            List<ModifierEntry> statBonus = equipmentItem.getStatBonus();
            statBonus.forEach(modifierEntry -> {
                switch (modifierEntry.modifierType()) {
                    case ModifierEntry.ModifierType.BASE -> modifierEntry.apply(character.getStats());
                    case ModifierEntry.ModifierType.MAX -> modifierEntry.apply(character.getMaxStats());
                    case ModifierEntry.ModifierType.ALL -> {
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
                    case ModifierEntry.ModifierType.BASE -> modifierEntry.remove(character.getStats());
                    case ModifierEntry.ModifierType.MAX -> modifierEntry.remove(character.getMaxStats());
                    case ModifierEntry.ModifierType.ALL -> {
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
