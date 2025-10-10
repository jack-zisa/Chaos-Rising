package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.stream.IntStream;

public record MoveCharacterS2C(byte[] data) {
    public MoveCharacterS2C(float x, float y, float xv, float yv) {
        this(MoveEntitiesS2C.pack(x, y, xv, yv));
    }

    public static final Codec<MoveCharacterS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.BYTE.listOf().fieldOf("data").forGetter(entry -> IntStream.range(0, entry.data.length).mapToObj(i -> entry.data[i]).toList())
        ).apply(instance, data -> {
            byte[] arr = new byte[data.size()];
            for (int i = 0; i < data.size(); i++) {
                arr[i] = data.get(i);
            }
            return new MoveCharacterS2C(arr);
        });
    });
}
