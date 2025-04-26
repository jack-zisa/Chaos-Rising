package dev.creoii.chaos.loot;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.ServerGame;
import dev.creoii.chaos.item.ServerItem;
import dev.creoii.chaos.item.ItemStack;

public record LootEntry(String item, int weight, int minCount, int maxCount) {
    public ItemStack roll(ServerGame game) {
        ServerItem item = game.getDataManager().getItem(item());
        if (item == null)
            return ItemStack.EMPTY;
        return new ItemStack(item, minCount + (int) (Math.random() * (maxCount - minCount + 1)));
    }

    public static LootEntry parse(JsonValue jsonValue) {
        String item = jsonValue.getString("item");
        int weight = jsonValue.has("weight") ? jsonValue.getInt("weight") : 1;
        int minCount = jsonValue.has("min_count") ? jsonValue.getInt("min_count") : 1;
        int maxCount = jsonValue.has("max_count") ? jsonValue.getInt("max_count") : 1;
        return new LootEntry(item, weight, minCount, maxCount);
    }
}
