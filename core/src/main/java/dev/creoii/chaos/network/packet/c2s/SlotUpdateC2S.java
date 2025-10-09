package dev.creoii.chaos.network.packet.c2s;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.SlotEntry;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.UUID;

public record SlotUpdateC2S(UUID uuid, Action action, InventoryType from, InventoryType to, SlotEntry fromSlot, SlotEntry toSlot) implements Serializable {
    public static final Codec<SlotUpdateC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codecs.UUID.fieldOf("uuid").forGetter(SlotUpdateC2S::uuid),
            Action.CODEC.fieldOf("action").forGetter(SlotUpdateC2S::action),
            InventoryType.CODEC.fieldOf("from").forGetter(SlotUpdateC2S::from),
            InventoryType.CODEC.fieldOf("to").forGetter(SlotUpdateC2S::to),
            SlotEntry.CODEC.fieldOf("from_slot").forGetter(SlotUpdateC2S::fromSlot),
            SlotEntry.CODEC.fieldOf("to_slot").forGetter(SlotUpdateC2S::toSlot)
        ).apply(instance, SlotUpdateC2S::new);
    });

    public enum Action {
        MOVE,
        SWAP,
        QUICK_MOVE;

        public static final Codec<Action> CODEC = Codec.STRING.xmap(s -> Action.valueOf(s.toUpperCase()), action -> action.name().toLowerCase());
    }
}
