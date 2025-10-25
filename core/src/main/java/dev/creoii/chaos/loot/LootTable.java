package dev.creoii.chaos.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.item.ItemStack;
import dev.creoii.chaos.util.Identifiable;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.ArrayList;
import java.util.List;

public class LootTable implements Identifiable {
    public static final LootTable EMPTY = new LootTable("empty");
    public static final Codec<LootTable> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("id").forGetter(lootTable -> lootTable.id),
            LootEntry.CODEC.listOf().fieldOf("entries").forGetter(lootTable -> lootTable.entries)
        ).apply(instance, (id, entries) -> {
            LootTable lootTable = new LootTable(id);
            entries.forEach(lootTable::addEntry);
            return lootTable;
        });
    });
    public static final Codec<LootTable> ID_CODEC = Codec.STRING.xmap(DataManager::getLootTable, lootTable -> lootTable.id);
    private final String id;
    private final ObjectList<LootEntry> entries = new ObjectArrayList<>();

    public LootTable(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    public List<LootEntry> getEntries() {
        return entries;
    }

    public void addEntry(LootEntry entry) {
        entries.add(entry);
    }

    public List<ItemStack> roll(Game game, int rolls) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < rolls; ++i) {
            LootEntry entry = getWeightedRandomEntry();
            if (entry != null) {
                stacks.add(entry.roll(game));
            }
        }
        return stacks;
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
