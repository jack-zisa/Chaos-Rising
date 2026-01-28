package dev.creoii.chaos.util.provider.stringprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Provider;

import java.util.List;

public record ConcatStringProvider(List<StringProvider> values) implements StringProvider {
    public static final MapCodec<ConcatStringProvider> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            StringProvider.CODEC.listOf().fieldOf("values").forGetter(ConcatStringProvider::values)
        ).apply(instance, ConcatStringProvider::new)
    );

    @Override
    public Type getType() {
        return Type.CONCAT;
    }

    @Override
    public String get(ContextProvider context) {
        return String.join("", values.stream().map(s -> s.get(context)).toList());
    }

    @Override
    public Provider<String> optimize() {
        if (values.stream().allMatch(s -> s instanceof ConstantStringProvider)) {
            return new ConstantStringProvider(String.join("", values.stream().map(s -> ((ConstantStringProvider) s).value()).toList()));
        }
        return StringProvider.super.optimize();
    }
}
