package dev.creoii.chaos.network.c2s;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.io.Serializable;
import java.util.Arrays;

public record ExecuteCommandC2S(int id, String commandType, String[] args) implements Serializable {
    public static final Codec<ExecuteCommandC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(ExecuteCommandC2S::id),
            Codec.STRING.fieldOf("command_type").forGetter(ExecuteCommandC2S::commandType),
            Codec.STRING.listOf().fieldOf("args").forGetter(executeCommandC2S -> Arrays.asList(executeCommandC2S.args))
        ).apply(instance, (uuid, commandType, args) -> new ExecuteCommandC2S(uuid, commandType, args.toArray(new String[]{})));
    });
}
