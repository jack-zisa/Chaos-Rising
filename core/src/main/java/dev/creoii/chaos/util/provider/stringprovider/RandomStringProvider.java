package dev.creoii.chaos.util.provider.stringprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

import java.util.List;

public record RandomStringProvider(List<StringProvider> values) implements StringProvider {
    public static final MapCodec<RandomStringProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            StringProvider.CODEC.listOf().fieldOf("values").forGetter(RandomStringProvider::values)
        ).apply(instance, RandomStringProvider::new)
    );

    @Override
    public Type getType() {
        return Type.RANDOM;
    }

    @Override
    public String get(ContextProvider context) {
        return context.has(ComponentTypes.RANDOM) ? values.get(context.get(ComponentTypes.RANDOM).nextInt(values.size())).get(context) : "";
    }
}
