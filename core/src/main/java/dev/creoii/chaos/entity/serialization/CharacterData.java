package dev.creoii.chaos.entity.serialization;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.List;

public record CharacterData(StatContainer baseStats, StatContainer maxStats, List<List<SlotEntry>> slots) implements EntityCustomData {
    public static final MapCodec<CharacterData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        StatContainer.CODEC.fieldOf("base_stats").forGetter(CharacterData::baseStats),
        StatContainer.CODEC.fieldOf("max_stats").forGetter(CharacterData::maxStats),
        SlotEntry.CODEC.listOf().listOf().fieldOf("slots").forGetter(CharacterData::slots)
    ).apply(instance, CharacterData::new));

    public CharacterData(StatContainer baseStats, StatContainer maxStats, Slot[][] slots) {
        this(baseStats, maxStats, Slot.toSlotEntries(slots));
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.CHARACTER;
    }

    @Override
    public MapCodec<CharacterData> getCodec() {
        return CODEC;
    }
}
