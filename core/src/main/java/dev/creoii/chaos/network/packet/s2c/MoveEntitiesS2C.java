package dev.creoii.chaos.network.packet.s2c;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.Codecs;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record MoveEntitiesS2C(List<Entry> entries) implements Serializable {
    public static final Codec<MoveEntitiesS2C> CODEC = Entry.CODEC.listOf().xmap(MoveEntitiesS2C::new, MoveEntitiesS2C::entries);

    public record Entry(UUID uuid, float x, float y, float xv, float yv) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                Codecs.UUID.fieldOf("uuid").forGetter(Entry::uuid),
                Codec.FLOAT.optionalFieldOf("x").forGetter(entry -> entry.x == 0f ? Optional.empty() : Optional.of(entry.x)),
                Codec.FLOAT.optionalFieldOf("y").forGetter(entry -> entry.y == 0f ? Optional.empty() : Optional.of(entry.y)),
                Codec.FLOAT.optionalFieldOf("xv").forGetter(entry -> entry.xv == 0f ? Optional.empty() : Optional.of(entry.xv)),
                Codec.FLOAT.optionalFieldOf("yv").forGetter(entry -> entry.yv == 0f ? Optional.empty() : Optional.of(entry.yv))
            ).apply(instance, (uuid, x, y, xv, yv) -> new Entry(uuid, x.orElse(0f), y.orElse(0f), xv.orElse(0f), yv.orElse(0f)));
        });
    }
}
