package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.PacketUtils;

import java.io.Serializable;

public record AttackC2S(int id, Slot slot, float mouseX, float mouseY) implements Serializable {
    public static final Codec<AttackC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(AttackC2S::id),
            Slot.CODEC.fieldOf("slot").forGetter(AttackC2S::slot),
            Codec.FLOAT.fieldOf("mouse_x").forGetter(AttackC2S::mouseX),
            Codec.FLOAT.fieldOf("mouse_y").forGetter(AttackC2S::mouseY)
        ).apply(instance, AttackC2S::new);
    });

    public static void write(Output output, AttackC2S o) {
        output.writeInt(o.id);
        PacketUtils.writeSlot(output, o.slot);
        output.writeFloat(o.mouseX);
        output.writeFloat(o.mouseY);
    }

    public static AttackC2S read(Input input) {
        return new AttackC2S(input.readInt(), PacketUtils.readSlot(input), input.readFloat(), input.readFloat());
    }
}
