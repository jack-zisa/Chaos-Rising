package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.effect.StatusEffect;
import dev.creoii.chaos.network.PacketUtils;

public record StatusEffectS2C(int id, StatusEffect.Instance instance, boolean add) {
    public static final Codec<StatusEffectS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(StatusEffectS2C::id),
            StatusEffect.Instance.CODEC.fieldOf("effect").forGetter(StatusEffectS2C::instance),
            Codec.BOOL.fieldOf("add").forGetter(StatusEffectS2C::add)
        ).apply(instance, StatusEffectS2C::new);
    });

    public static void write(Output output, StatusEffectS2C o) {
        output.writeInt(o.id);
        PacketUtils.writeStatusEffectInstance(output, o.instance);
        output.writeBoolean(o.add);
    }

    public static StatusEffectS2C read(Input input) {
        return new StatusEffectS2C(input.readInt(), PacketUtils.readStatusEffectInstance(input), input.readBoolean());
    }
}
