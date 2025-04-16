package dev.creoii.chaos.loot;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LootTable {
    private final List<LootEntry> entries = new ArrayList<>();

    public static LootTable parse(JsonValue jsonValue) {
        LootTable lootTable = new LootTable();
        jsonValue.get("entries").forEach(jsonValue1 -> {
            lootTable.addEntry(LootEntry.parse(jsonValue1));
        });
        return lootTable;
    }

    public void addEntry(LootEntry entry) {
        entries.add(entry);
    }

    public List<ItemStack> roll(Game game, int rolls) {
        List<ItemStack> result = new ArrayList<>();
        for (int i = 0; i < rolls; i++) {
            LootEntry entry = getWeightedRandomEntry();
            if (entry != null) {
                result.add(entry.roll(game));
            }
        }
        return result;
    }

    private LootEntry getWeightedRandomEntry() {
        int totalWeight = entries.stream().mapToInt(LootEntry::weight).sum();
        if (totalWeight <= 0)
            return null;

        int r = (int) (Math.random() * totalWeight);
        for (LootEntry entry : entries) {
            r -= entry.weight();
            if (r < 0)
                return entry;
        }
        return null;
    }
}
