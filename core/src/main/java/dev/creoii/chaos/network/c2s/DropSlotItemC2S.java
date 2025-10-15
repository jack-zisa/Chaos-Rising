package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.PacketUtils;

public record DropSlotItemC2S(int id, Slot slot) {
    public static final Codec<DropSlotItemC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(DropSlotItemC2S::id),
            Slot.CODEC.fieldOf("slot").forGetter(DropSlotItemC2S::slot)
        ).apply(instance, DropSlotItemC2S::new);
    });

    public static void write(Output output, DropSlotItemC2S o) {
        output.writeInt(o.id);
        PacketUtils.writeSlot(output, o.slot);
    }

    public static DropSlotItemC2S read(Input input) {
        return new DropSlotItemC2S(input.readInt(), PacketUtils.readSlot(input));
    }
}
