package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

public record CharacterLeaveC2S(int id) {
    public static final Codec<CharacterLeaveC2S> CODEC = Codec.INT.xmap(CharacterLeaveC2S::new, CharacterLeaveC2S::id);

    public static void write(Output output, CharacterLeaveC2S o) {
        output.writeInt(o.id);
    }

    public static CharacterLeaveC2S read(Input input) {
        return new CharacterLeaveC2S(input.readInt());
    }
}
