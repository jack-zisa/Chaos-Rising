package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.PacketUtils;

public record ClickSlotC2S(int id, Slot slot, boolean consume) {
    public static final Codec<ClickSlotC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(ClickSlotC2S::id),
            Slot.CODEC.fieldOf("slot").forGetter(ClickSlotC2S::slot),
            Codec.BOOL.fieldOf("consume").forGetter(ClickSlotC2S::consume)
        ).apply(instance, ClickSlotC2S::new);
    });

    public static void write(Output output, ClickSlotC2S o) {
        output.writeInt(o.id);
        PacketUtils.writeSlot(output, o.slot);
        output.writeBoolean(o.consume);
    }

    public static ClickSlotC2S read(Input input) {
        return new ClickSlotC2S(input.readInt(), PacketUtils.readSlot(input), input.readBoolean());
    }
}
