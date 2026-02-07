package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.inventory.Slot;
import dev.creoii.chaos.network.PacketUtils;

public record AbilityC2S(int id, Slot slot, float mouseX, float mouseY) {
    public static final Codec<AbilityC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(AbilityC2S::id),
            Slot.CODEC.fieldOf("slot").forGetter(AbilityC2S::slot),
            Codec.FLOAT.fieldOf("mouse_x").forGetter(AbilityC2S::mouseX),
            Codec.FLOAT.fieldOf("mouse_y").forGetter(AbilityC2S::mouseY)
        ).apply(instance, AbilityC2S::new);
    });

    public static void write(Output output, AbilityC2S o) {
        output.writeInt(o.id);
        PacketUtils.writeSlot(output, o.slot);
        output.writeFloat(o.mouseX);
        output.writeFloat(o.mouseY);
    }

    public static AbilityC2S read(Input input) {
        return new AbilityC2S(input.readInt(), PacketUtils.readSlot(input), input.readFloat(), input.readFloat());
    }
}
