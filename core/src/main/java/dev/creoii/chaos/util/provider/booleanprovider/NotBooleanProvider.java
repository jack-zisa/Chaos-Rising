package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.provider.Provider;

public record NotBooleanProvider(BooleanProvider value) implements BooleanProvider {
    public static final MapCodec<NotBooleanProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            BooleanProvider.CODEC.fieldOf("value").forGetter(NotBooleanProvider::value)
        ).apply(instance, NotBooleanProvider::new);
    });

    @Override
    public Type getType() {
        return Type.NOT;
    }

    @Override
    public Boolean get(ContextProvider context) {
        return !value.get(context);
    }

    @Override
    public Provider<Boolean> optimize() {
        if (value instanceof ConstantBooleanProvider(boolean value1)) {
            return new ConstantBooleanProvider(!value1);
        }
        return BooleanProvider.super.optimize();
    }

    @Override
    public NotBooleanProvider copy() {
        return new NotBooleanProvider(value.copy());
    }

    public NotBooleanProvider init(int startTime) {
        value.init(startTime);
        return this;
    }
}
