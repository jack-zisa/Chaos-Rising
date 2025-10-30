package dev.creoii.chaos.util.provider.stringprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import javax.annotation.Nullable;
import java.util.List;

public record ConcatStringProvider(List<String> values) implements StringProvider {
    public static final MapCodec<ConcatStringProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.STRING.listOf().fieldOf("values").forGetter(ConcatStringProvider::values)
        ).apply(instance, ConcatStringProvider::new)
    );

    @Override
    public Type getType() {
        return Type.CONCAT;
    }

    @Override
    public String get(Context context) {
        return String.join("", values);
    }
}
