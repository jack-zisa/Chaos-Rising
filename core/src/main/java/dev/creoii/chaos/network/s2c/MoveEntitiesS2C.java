package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public record MoveEntitiesS2C(List<Entry> entries) {
    public static final Codec<MoveEntitiesS2C> CODEC = Entry.CODEC.listOf().xmap(MoveEntitiesS2C::new, MoveEntitiesS2C::entries);

    public record Entry(int id, float x, float y, float xv, float yv) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                Codec.INT.fieldOf("id").forGetter(Entry::id),
                Codec.FLOAT.fieldOf("x").forGetter(Entry::x),
                Codec.FLOAT.fieldOf("y").forGetter(Entry::y),
                Codec.FLOAT.fieldOf("xv").forGetter(Entry::xv),
                Codec.FLOAT.fieldOf("yv").forGetter(Entry::yv)
            ).apply(instance, Entry::new);
        });
    }

    public static void write(Output output, MoveEntitiesS2C o) {
        output.writeInt(o.entries.size());
        for (Entry entry : o.entries) {
            output.writeInt(entry.id);
            output.writeFloat(entry.x);
            output.writeFloat(entry.y);
            output.writeFloat(entry.xv);
            output.writeFloat(entry.yv);
        }
    }

    public static MoveEntitiesS2C read(Input input) {
        int count = input.readInt();
        List<Entry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            entries.add(new Entry(input.readInt(), input.readFloat(), input.readFloat(), input.readFloat(), input.readFloat()));
        }
        return new MoveEntitiesS2C(entries);
    }
}
