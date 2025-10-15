package dev.creoii.chaos.network.c2s;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;

public record ExecuteCommandC2S(int id, String commandType, String[] args) {
    public static final Codec<ExecuteCommandC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(ExecuteCommandC2S::id),
            Codec.STRING.fieldOf("command_type").forGetter(ExecuteCommandC2S::commandType),
            Codec.STRING.listOf().fieldOf("args").forGetter(executeCommandC2S -> Arrays.asList(executeCommandC2S.args))
        ).apply(instance, (uuid, commandType, args) -> new ExecuteCommandC2S(uuid, commandType, args.toArray(new String[]{})));
    });

    public static void write(Output output, ExecuteCommandC2S o) {
        output.writeInt(o.id);
        output.writeString(o.commandType);
        output.writeInt(o.args.length);
        for (String s : o.args) {
            output.writeString(s);
        }
    }

    public static ExecuteCommandC2S read(Input input) {
        int id = input.readInt();
        String commandType = input.readString();
        int argCount = input.readInt();
        String[] args = new String[argCount];
        for (int i = 0; i < argCount; i++) {
            args[i] = input.readString();
        }
        return new ExecuteCommandC2S(id, commandType, args);
    }
}
