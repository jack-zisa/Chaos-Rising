package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.serialization.EntityCustomData;

import java.io.Serializable;
import java.util.List;
import java.util.stream.IntStream;

public record SpawnEntitiesS2C(List<Entry> entries) implements Serializable {
    public static final Codec<SpawnEntitiesS2C> CODEC = Entry.CODEC.listOf().xmap(SpawnEntitiesS2C::new, SpawnEntitiesS2C::entries);

    public record Entry(int id, byte[] data, EntityCustomData customData) {
        public Entry(int id, float x, float y, EntityCustomData customData) {
            this(id, pack(x, y), customData);
        }

        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                Codec.INT.fieldOf("id").forGetter(Entry::id),
                Codec.BYTE.listOf().fieldOf("data").forGetter(entry -> IntStream.range(0, entry.data.length).mapToObj(i -> entry.data[i]).toList()),
                EntityCustomData.CODEC.fieldOf("custom_data").forGetter(Entry::customData)
            ).apply(instance, (id, data, customData) -> {
                byte[] arr = new byte[data.size()];
                for (int i = 0; i < data.size(); i++) {
                    arr[i] = data.get(i);
                }
                return new Entry(id, arr, customData);
            });
        });
    }

    public static byte[] pack(float x, float y) {
        byte[] bytes = new byte[16];
        int i = 0;
        for (float f : new float[]{x, y}) {
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
            int bits = ((bytes[base] & 0xff) << 24) | ((bytes[base + 1] & 0xff) << 16) | ((bytes[base + 2] & 0xff) << 8) | (bytes[base + 3] & 0xff);
            floats[i] = Float.intBitsToFloat(bits);
        }
        return floats;
    }
}
