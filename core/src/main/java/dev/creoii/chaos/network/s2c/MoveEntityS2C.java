package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.stream.IntStream;

public record MoveEntityS2C(int id, byte[] data) {
    public MoveEntityS2C(int id, float x, float y, float xv, float yv) {
        this(id, MoveEntitiesS2C.pack(x, y, xv, yv));
    }

    public static final Codec<MoveEntityS2C> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
            Codec.INT.fieldOf("id").forGetter(MoveEntityS2C::id),
            Codec.BYTE.listOf().fieldOf("data").forGetter(entry -> IntStream.range(0, entry.data.length).mapToObj(i -> entry.data[i]).toList())
        ).apply(instance, (id, data) -> {
            byte[] arr = new byte[data.size()];
            for (int i = 0; i < data.size(); i++) {
                arr[i] = data.get(i);
            }
            return new MoveEntityS2C(id, arr);
        });
    });
}
