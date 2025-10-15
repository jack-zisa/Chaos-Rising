package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.PacketUtils;

public record SlotUpdateC2S(int id, Action action, InventoryType from, InventoryType to, Slot fromSlot, Slot toSlot) {
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

    public static void write(Output output, SlotUpdateC2S o) {
        output.writeInt(o.id);
        PacketUtils.writeEnum(output, o.action);
        PacketUtils.writeEnum(output, o.from);
        PacketUtils.writeEnum(output, o.to);
        PacketUtils.writeSlot(output, o.fromSlot);
        PacketUtils.writeSlot(output, o.toSlot);
    }

    public static SlotUpdateC2S read(Input input) {
        return new SlotUpdateC2S(input.readInt(), PacketUtils.readEnum(Action.class, input), PacketUtils.readEnum(InventoryType.class, input), PacketUtils.readEnum(InventoryType.class, input), PacketUtils.readSlot(input), PacketUtils.readSlot(input));
    }

    public enum Action {
        MOVE,
        SWAP,
        QUICK_MOVE;

        public static final Codec<Action> CODEC = Codec.STRING.xmap(s -> Action.valueOf(s.toUpperCase()), action -> action.name().toLowerCase());
    }
}
