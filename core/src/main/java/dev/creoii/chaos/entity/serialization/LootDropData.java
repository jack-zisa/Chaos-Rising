package dev.creoii.chaos.entity.serialization;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.util.EntityGroup;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public record LootDropData(Optional<List<List<Slot>>> slots) implements EntityCustomData {
    public static final MapCodec<LootDropData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Slot.CODEC.listOf().listOf().optionalFieldOf("slots").forGetter(LootDropData::slots)
    ).apply(instance, LootDropData::new));

    public LootDropData(@Nullable Slot[][] slots) {
        this(slots == null ? Optional.empty() : Optional.of(Slot.toSlotListArray(slots)));
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
