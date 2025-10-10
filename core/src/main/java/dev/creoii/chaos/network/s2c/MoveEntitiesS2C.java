package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record MoveEntitiesS2C(List<Entry> entries) implements Serializable {
    public static final Codec<MoveEntitiesS2C> CODEC = Entry.CODEC.listOf().xmap(MoveEntitiesS2C::new, MoveEntitiesS2C::entries);

    public record Entry(UUID uuid, long packed) {
        public Entry(UUID uuid, float x, float y, float xv, float yv) {
            this(uuid, pack(x, y, xv, yv));
        }

        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                Codecs.UUID.fieldOf("uuid").forGetter(Entry::uuid),
                Codec.LONG.fieldOf("packed").forGetter(Entry::packed)
            ).apply(instance, Entry::new);
        });
    }

    public static long pack(float x, float y, float xv, float yv) {
        int ix = Math.round(x * 10f);
        int iy = Math.round(y * 10f);
        int ivx = Math.round(xv * 100f);
        int ivy = Math.round(yv * 100f);

        long bx = ix + 16384;
        long by = iy + 16384;
        long bxv = ivx + 2048;
        long byv = ivy + 2048;

        return (bx  & 0x7fff) << 49 | (by  & 0x7fff) << 34 | (bxv & 0xfff) << 22 | (byv & 0xfff);
    }

    public static float[] unpack(long packed) {
        int bx = (int)((packed >> 49) & 0x7fff);
        int by = (int)((packed >> 34) & 0x7fff);
        int bxv = (int)((packed >> 22) & 0xfff);
        int byv = (int)(packed & 0xfff);

        int ix = bx - 16384;
        int iy = by - 16384;
        int ivx = bxv - 2048;
        int ivy = byv - 2048;

        return new float[] {ix / 10f, iy / 10f, ivx / 100f, ivy / 100f};
    }
}
