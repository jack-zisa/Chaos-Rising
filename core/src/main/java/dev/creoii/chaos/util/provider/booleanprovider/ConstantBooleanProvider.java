package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ConstantBooleanProvider(boolean value) implements BooleanProvider {
    public static final ConstantBooleanProvider TRUE = new ConstantBooleanProvider(true);
    public static final ConstantBooleanProvider FALSE = new ConstantBooleanProvider(false);
    public static final MapCodec<ConstantBooleanProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.BOOL.fieldOf("value").forGetter(ConstantBooleanProvider::value)
        ).apply(instance, (value) -> {
            if (value) return TRUE;
            else return FALSE;
        });
    });

    @Override
    public Type getType() {
        return Type.CONSTANT;
    }

    @Override
    public Boolean get(Context context) {
        return value;
    }

    @Override
    public ConstantBooleanProvider copy() {
        return this;
    }

    public ConstantBooleanProvider init(int startTime) {
        return this;
    }
}
