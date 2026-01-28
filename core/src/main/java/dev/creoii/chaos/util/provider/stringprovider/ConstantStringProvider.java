package dev.creoii.chaos.util.provider.stringprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;

public record ConstantStringProvider(String value) implements StringProvider {
    public static final MapCodec<ConstantStringProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.STRING.fieldOf("value").forGetter(ConstantStringProvider::value)
        ).apply(instance, ConstantStringProvider::new)
    );

    @Override
    public Type getType() {
        return Type.CONSTANT;
    }

    @Override
    public String get(ContextProvider context) {
        return value;
    }
}
