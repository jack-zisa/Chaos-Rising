package dev.creoii.chaos.network.c2s;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;

import java.io.Serializable;

public record SlotUpdateC2S(int id, Action action, InventoryType from, InventoryType to, Slot fromSlot, Slot toSlot) implements Serializable {
    public static final Codec<SlotUpdateC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(SlotUpdateC2S::id),
            Action.CODEC.fieldOf("action").forGetter(SlotUpdateC2S::action),
            InventoryType.CODEC.fieldOf("from").forGetter(SlotUpdateC2S::from),
            InventoryType.CODEC.fieldOf("to").forGetter(SlotUpdateC2S::to),
            Slot.CODEC.fieldOf("from_slot").forGetter(SlotUpdateC2S::fromSlot),
            Slot.CODEC.fieldOf("to_slot").forGetter(SlotUpdateC2S::toSlot)
        ).apply(instance, SlotUpdateC2S::new);
    });

    public enum Action {
        MOVE,
        SWAP,
        QUICK_MOVE;

        public static final Codec<Action> CODEC = Codec.STRING.xmap(s -> Action.valueOf(s.toUpperCase()), action -> action.name().toLowerCase());
    }
}
