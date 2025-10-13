package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.io.Serializable;
import java.util.List;

public record DisplayEntitiesS2C(List<Entry> entries) implements Serializable {
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
}
