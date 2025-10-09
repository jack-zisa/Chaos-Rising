package dev.creoii.chaos.entity.serialization;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.util.EntityGroup;

import java.util.List;

public record LootDropData(List<List<SlotEntry>> slots) implements EntityCustomData {
    public static final MapCodec<LootDropData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SlotEntry.CODEC.listOf().listOf().fieldOf("slots").forGetter(LootDropData::slots)
    ).apply(instance, LootDropData::new));

    public LootDropData(Slot[][] slots) {
        this(Slot.toSlotEntries(slots));
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.LOOT_DROP;
    }

    @Override
    public MapCodec<LootDropData> getCodec() {
        return CODEC;
    }
}
