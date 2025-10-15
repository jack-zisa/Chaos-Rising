package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.serialization.EntityCustomData;
import dev.creoii.chaos.network.PacketUtils;

import java.util.ArrayList;
import java.util.List;

public record SpawnEntitiesS2C(List<Entry> entries) {
    public static final Codec<SpawnEntitiesS2C> CODEC = Entry.CODEC.listOf().xmap(SpawnEntitiesS2C::new, SpawnEntitiesS2C::entries);

    public static void write(Output output, SpawnEntitiesS2C o) {
        output.writeInt(o.entries().size());
        for (Entry entry : o.entries) {
            output.writeInt(entry.id);
            output.writeFloat(entry.x);
            output.writeFloat(entry.y);
            PacketUtils.writeCustomEntityData(output, entry.customData);
        }
    }

    public static SpawnEntitiesS2C read(Input input) {
        int count = input.readInt();
        List<Entry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            entries.add(new Entry(input.readInt(), input.readFloat(), input.readFloat(), PacketUtils.readCustomEntityData(input)));
        }
        return new SpawnEntitiesS2C(entries);
    }

    public record Entry(int id, float x, float y, EntityCustomData customData) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                Codec.INT.fieldOf("id").forGetter(Entry::id),
                Codec.FLOAT.fieldOf("x").forGetter(Entry::x),
                Codec.FLOAT.fieldOf("y").forGetter(Entry::y),
                EntityCustomData.CODEC.fieldOf("custom_data").forGetter(Entry::customData)
            ).apply(instance, Entry::new);
        });
    }
}
