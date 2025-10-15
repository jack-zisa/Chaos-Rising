package dev.creoii.chaos.entity.serialization;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.PacketUtils;
import dev.creoii.chaos.util.EntityGroup;

import javax.annotation.Nullable;
import java.util.ArrayList;
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

    @Override
    public void write(Output output) {
        if (slots.isPresent()) {
            output.writeBoolean(true);
            List<List<Slot>> outerList = slots.get();

            output.writeInt(outerList.size());

            for (List<Slot> innerList : outerList) {
                output.writeInt(innerList.size());
                for (Slot slot : innerList) {
                    PacketUtils.writeSlot(output, slot);
                }
            }
        } else output.writeBoolean(false);
    }

    public static LootDropData read(Input input) {
        boolean hasSlots = input.readBoolean();

        if (!hasSlots) {
            return new LootDropData((Slot[][]) null);
        }

        int outerSize = input.readInt();
        List<List<Slot>> outerList = new ArrayList<>(outerSize);

        for (int i = 0; i < outerSize; i++) {
            int innerSize = input.readInt();
            List<Slot> innerList = new ArrayList<>(innerSize);

            for (int j = 0; j < innerSize; j++) {
                innerList.add(PacketUtils.readSlot(input));
            }

            outerList.add(innerList);
        }

        return new LootDropData(Optional.of(outerList));
    }
}
