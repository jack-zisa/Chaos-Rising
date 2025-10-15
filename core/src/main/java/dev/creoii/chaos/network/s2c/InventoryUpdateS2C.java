package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.InventoryType;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.PacketUtils;

import java.util.ArrayList;
import java.util.List;

public record InventoryUpdateS2C(InventoryType type, List<Slot> slots) {
    public static final Codec<InventoryUpdateS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            InventoryType.CODEC.fieldOf("type").forGetter(InventoryUpdateS2C::type),
            Slot.CODEC.listOf().fieldOf("slots").forGetter(InventoryUpdateS2C::slots)
        ).apply(instance, InventoryUpdateS2C::new);
    });

    public static void write(Output output, InventoryUpdateS2C o) {
        PacketUtils.writeEnum(output, o.type);
        output.writeInt(o.slots.size());
        for (Slot slot : o.slots) {
            PacketUtils.writeSlot(output, slot);
        }
    }

    public static InventoryUpdateS2C read(Input input) {
        InventoryType type = PacketUtils.readEnum(InventoryType.class, input);
        int count = input.readInt();
        List<Slot> slots = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            slots.add(PacketUtils.readSlot(input));
        }
        return new InventoryUpdateS2C(type, slots);
    }
}
