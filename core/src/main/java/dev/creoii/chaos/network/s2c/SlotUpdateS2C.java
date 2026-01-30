package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.PacketUtils;

public record SlotUpdateS2C(int id, InventoryType inventory, Slot slot) {
    public static final Codec<SlotUpdateS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(SlotUpdateS2C::id),
            InventoryType.CODEC.fieldOf("inventory").forGetter(SlotUpdateS2C::inventory),
            Slot.CODEC.fieldOf("slot").forGetter(SlotUpdateS2C::slot)
        ).apply(instance, SlotUpdateS2C::new);
    });

    public static void write(Output output, SlotUpdateS2C o) {
        output.writeInt(o.id);
        PacketUtils.writeEnum(output, o.inventory);
        PacketUtils.writeSlot(output, o.slot);
    }

    public static SlotUpdateS2C read(Input input) {
        return new SlotUpdateS2C(input.readInt(), PacketUtils.readEnum(InventoryType.class, input), PacketUtils.readSlot(input));
    }
}
