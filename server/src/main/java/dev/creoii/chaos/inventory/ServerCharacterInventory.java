package dev.creoii.chaos.inventory;

import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.network.packet.s2c.InventoryUpdateS2C;

import java.util.ArrayList;
import java.util.List;

public class ServerCharacterInventory extends CharacterInventory {
    @Override
    public void onAddItemToSlot(Slot slot, ItemStack stack) {
        List<SlotEntry> slots = new ArrayList<>();
        slots.add(new SlotEntry(slot.getR(), slot.getC(), slot.getType(), stack, slot.isActive()));
        ((ServerGame) getCharacter().getGame()).getServer().sendToTCP(getCharacter().getConnectionId(), new InventoryUpdateS2C(InventoryType.MAIN, slots));
        super.onAddItemToSlot(slot, stack);
    }

    @Override
    public void onRemoveItemFromSlot(Slot slot, ItemStack stack) {
        List<SlotEntry> slots = new ArrayList<>();
        slots.add(new SlotEntry(slot.getR(), slot.getC(), slot.getType(), ItemStack.EMPTY, slot.isActive()));
        ((ServerGame) getCharacter().getGame()).getServer().sendToTCP(getCharacter().getConnectionId(), new InventoryUpdateS2C(InventoryType.MAIN, slots));
        super.onRemoveItemFromSlot(slot, stack);
    }
}
