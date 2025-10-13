package dev.creoii.chaos.entity.serialization;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.util.EntityGroup;
import dev.creoii.chaos.util.stat.StatContainer;

import java.util.List;
import java.util.Optional;

public record CharacterData(StatContainer baseStats, StatContainer maxStats, Optional<List<List<Slot>>> slots) implements EntityCustomData {
    public static final MapCodec<CharacterData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        StatContainer.STAT_CODEC.fieldOf("base_stats").forGetter(CharacterData::baseStats),
        StatContainer.STAT_CODEC.optionalFieldOf("max_stats").forGetter(characterData -> characterData.maxStats.equals(characterData.baseStats) ? Optional.empty() : Optional.of(characterData.maxStats)),
        Slot.CODEC.listOf().listOf().optionalFieldOf("slots").forGetter(CharacterData::slots)
    ).apply(instance, (baseStats, maxStats, slots) -> new CharacterData(baseStats, maxStats.orElse(baseStats), slots)));

    public CharacterData(StatContainer baseStats, StatContainer maxStats, Slot[][] slots) {
        this(baseStats, maxStats, slots == null ? Optional.empty() : Optional.of(Slot.toSlotListArray(slots)));
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
