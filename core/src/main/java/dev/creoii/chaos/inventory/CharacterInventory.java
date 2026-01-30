package dev.creoii.chaos.inventory;

import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.item.EquipmentItem;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.c2s.SlotUpdateC2S;
import dev.creoii.chaos.network.s2c.InventoryUpdateS2C;
import dev.creoii.chaos.network.s2c.SlotUpdateS2C;
import dev.creoii.chaos.util.stat.ModifierEntry;

import javax.annotation.Nullable;
import java.util.List;

public class CharacterInventory extends Inventory {
    private final CharacterEntity character;

    public CharacterInventory(CharacterEntity character) {
        super(3, 4);
        this.character = character;
        getWeaponSlot().setType(Slot.Type.WEAPON);
        getAbilitySlot().setType(Slot.Type.ABILITY);
        getArmorSlot().setType(Slot.Type.ARMOR);
        getAccessorySlot().setType(Slot.Type.ACCESSORY);
    }

    public CharacterEntity getCharacter() {
        return character;
    }

    @Override
    public void onAddItemToSlot(Slot slot, ItemStack stack) {
        if (slot.getType() != Slot.Type.NONE && slot.getType().getItemPredicate().test(stack.getItem())) {
            if (stack.getItem() instanceof EquipmentItem equipmentItem) {
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
    }

    @Override
    public void onRemoveItemFromSlot(Slot slot, ItemStack stack) {
        if (slot.getType() != Slot.Type.NONE && slot.getType().getItemPredicate().test(stack.getItem())) {
            if (stack.getItem() instanceof EquipmentItem equipmentItem) {
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
    }

    @Nullable
    @Override
    public Slot addItem(ItemStack stack) {
        Slot slot = super.addItem(stack);
        if (slot != null && !character.getWorld().getGame().isClient())
            character.getWorld().getGame().getServer().sendToAllTCP(new InventoryUpdateS2C(character.getId(), InventoryType.MAIN, List.of(slot)));
        return slot;
    }

    @Override
    public void updateSlot(SlotUpdateC2S.Action action, Inventory from, Inventory to, Slot fromSlot, Slot toSlot) {
        super.updateSlot(action, from, to, fromSlot, toSlot);
        if (!character.getWorld().getGame().isClient())
            character.getWorld().getGame().getServer().sendToAllTCP(new InventoryUpdateS2C(character.getId(), InventoryType.MAIN, List.of(fromSlot, toSlot)));
    }

    @Override
    public void clearSlot(int r, int c) {
        super.clearSlot(r, c);
        if (!character.getWorld().getGame().isClient())
            character.getWorld().getGame().getServer().sendToAllTCP(new SlotUpdateS2C(character.getId(), InventoryType.MAIN, getSlots()[r][c]));
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
