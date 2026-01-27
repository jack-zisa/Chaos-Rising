package dev.creoii.chaos.util.provider.tileprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SimpleTileProvider(String value) implements TileProvider {
    public static final SimpleTileProvider EMPTY = new SimpleTileProvider("");
    public static final MapCodec<SimpleTileProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.STRING.fieldOf("value").forGetter(SimpleTileProvider::value)
        ).apply(instance, SimpleTileProvider::new)
    );

    @Override
    public Type getType() {
        return Type.SIMPLE;
    }

    @Override
    public String get(Context context) {
        return value;
    }
}
