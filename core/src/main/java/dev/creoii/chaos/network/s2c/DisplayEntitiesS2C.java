package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public record DisplayEntitiesS2C(List<Entry> entries) {
    public static final Codec<DisplayEntitiesS2C> CODEC = Entry.CODEC.listOf().xmap(DisplayEntitiesS2C::new, DisplayEntitiesS2C::entries);

    public record Entry(int id, String textureId, float scale) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                Codec.INT.fieldOf("id").forGetter(Entry::id),
                Codec.STRING.fieldOf("texture_id").forGetter(Entry::textureId),
                Codec.FLOAT.fieldOf("scale").forGetter(Entry::scale)
            ).apply(instance, Entry::new);
        });
    }

    public static void write(Output output, DisplayEntitiesS2C o) {
        output.writeInt(o.entries.size());
        for (Entry entry : o.entries) {
            output.writeInt(entry.id);
            output.writeString(entry.textureId);
            output.writeFloat(entry.scale);
        }
    }

    public static DisplayEntitiesS2C read(Input input) {
        int count = input.readInt();
        List<Entry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            entries.add(new Entry(input.readInt(), input.readString(), input.readFloat()));
        }
        return new DisplayEntitiesS2C(entries);
    }
}
