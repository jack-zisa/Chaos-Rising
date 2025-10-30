package dev.creoii.chaos.util.provider.stringprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
    public String get(Context context) {
        return values.get(context.random().nextInt(values.size())).get(context);
    }
}
