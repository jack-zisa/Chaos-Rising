package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public record SyncWorldSectionS2C(List<Entry> tiles) {
    public static final Codec<SyncWorldSectionS2C> CODEC = Entry.CODEC.listOf().xmap(SyncWorldSectionS2C::new, SyncWorldSectionS2C::tiles);

    public static void write(Output output, SyncWorldSectionS2C o) {
        output.writeInt(o.tiles.size());
        for (Entry entry : o.tiles) {
            output.writeInt(entry.x);
            output.writeInt(entry.y);
            output.writeString(entry.tile);
        }
    }

    public static SyncWorldSectionS2C read(Input input) {
        int count = input.readInt();
        List<Entry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            entries.add(new Entry(input.readInt(), input.readInt(), input.readString()));
        }
        return new SyncWorldSectionS2C(entries);
    }

    public record Entry(int x, int y, String tile) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                Codec.INT.fieldOf("x").forGetter(Entry::x),
                Codec.INT.fieldOf("y").forGetter(Entry::y),
                Codec.STRING.fieldOf("xv").forGetter(Entry::tile)
            ).apply(instance, Entry::new);
        });
    }
}
