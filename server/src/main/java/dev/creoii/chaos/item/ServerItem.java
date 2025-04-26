package dev.creoii.chaos.item;

import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.entity.inventory.Slot;
import dev.creoii.chaos.util.Rarity;

import java.util.UUID;

public class ServerItem extends Item {
    protected final ItemStack defaultStack;

    public ServerItem(String id, Type type, Rarity rarity) {
        super(id, type, rarity);
        defaultStack = new ItemStack(this);
    }

    public ItemStack getDefaultStack() {
        return defaultStack;
    }

    public boolean clickInSlot(ServerGame game, UUID characterUuid, Slot slot, ItemStack stack) {
        return false;
    }
}
