package dev.creoii.chaos.loot;

import dev.creoii.chaos.item.Item;
import dev.creoii.chaos.item.ItemStack;

public record LootEntry(Item item, int weight, int minCount, int maxCount) {
    public ItemStack roll() {
        return new ItemStack(item, minCount + (int) (Math.random() * (maxCount - minCount + 1)));
    }
}
