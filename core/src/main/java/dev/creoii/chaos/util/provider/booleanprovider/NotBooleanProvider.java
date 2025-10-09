package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
    public Boolean get(Context context) {
        return !value.get(context);
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
