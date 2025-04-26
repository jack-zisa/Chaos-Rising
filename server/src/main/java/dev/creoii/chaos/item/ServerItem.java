package dev.creoii.chaos.item;

import dev.creoii.chaos.util.Rarity;

public class ServerItem extends Item {
    protected final ItemStack defaultStack;

    public ServerItem(String id, Type type, Rarity rarity) {
        super(id, type, rarity);
        defaultStack = new ItemStack(this);
    }

    public ItemStack getDefaultStack() {
        return defaultStack;
    }
}
