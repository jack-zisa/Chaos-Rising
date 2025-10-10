package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.IntStream;

public record MoveEntitiesS2C(List<Entry> entries) implements Serializable {
    public static final Codec<MoveEntitiesS2C> CODEC = Entry.CODEC.listOf().xmap(MoveEntitiesS2C::new, MoveEntitiesS2C::entries);

    public record Entry(int id, byte[] data) {
        public Entry(int id, float x, float y, float xv, float yv) {
            this(id, pack(x, y, xv, yv));
        }

        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                Codec.INT.fieldOf("id").forGetter(Entry::id),
                Codec.BYTE.listOf().fieldOf("data").forGetter(entry -> IntStream.range(0, entry.data.length).mapToObj(i -> entry.data[i]).toList())
            ).apply(instance, (id, data) -> {
                byte[] arr = new byte[data.size()];
                for (int i = 0; i < data.size(); i++) {
                    arr[i] = data.get(i);
                }
                return new Entry(id, arr);
            });
        });
    }

    public static byte[] pack(float x, float y, float xv, float yv) {
        byte[] bytes = new byte[16];
        int i = 0;
        for (float f : new float[]{x, y, xv, yv}) {
            int bits = Float.floatToIntBits(f);
            bytes[i++] = (byte) (bits >>> 24);
            bytes[i++] = (byte) (bits >>> 16);
            bytes[i++] = (byte) (bits >>> 8);
            bytes[i++] = (byte) (bits);
        }
        return bytes;
    }

    public static float[] unpack(byte[] bytes) {
        float[] floats = new float[4];
        for (int i = 0; i < 4; i++) {
            int base = i * 4;
            int bits = ((bytes[base] & 0xFF) << 24) | ((bytes[base + 1] & 0xFF) << 16) | ((bytes[base + 2] & 0xFF) << 8) | (bytes[base + 3] & 0xFF);
            floats[i] = Float.intBitsToFloat(bits);
        }
        return floats;
    }
}
