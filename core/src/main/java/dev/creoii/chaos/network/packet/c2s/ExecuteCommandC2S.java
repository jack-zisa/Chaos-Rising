package dev.creoii.chaos.network.packet.c2s;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

public record ExecuteCommandC2S(UUID uuid, String commandType, String[] args) implements Serializable {
    public static final Codec<ExecuteCommandC2S> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codecs.UUID.fieldOf("uuid").forGetter(ExecuteCommandC2S::uuid),
            Codec.STRING.fieldOf("command_type").forGetter(ExecuteCommandC2S::commandType),
            Codec.STRING.listOf().fieldOf("args").forGetter(executeCommandC2S -> Arrays.asList(executeCommandC2S.args))
        ).apply(instance, (uuid, commandType, args) -> new ExecuteCommandC2S(uuid, commandType, args.toArray(new String[]{})));
    });
}
