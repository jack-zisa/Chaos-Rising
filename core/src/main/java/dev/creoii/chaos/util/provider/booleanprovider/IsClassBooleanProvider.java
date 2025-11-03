package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.CharacterEntityType;

public record IsClassBooleanProvider(String classId) implements BooleanProvider {
    public static final MapCodec<IsClassBooleanProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("class").forGetter(IsClassBooleanProvider::classId)
        ).apply(instance, IsClassBooleanProvider::new);
    });

    @Override
    public Type getType() {
        return Type.IS_CLASS;
    }

    @Override
    public Boolean get(Context context) {
        if (context.entity() instanceof CharacterEntity character) {
            return ((CharacterEntityType) character.getType()).characterClass().get().id().equals(classId);
        }
        return false;
    }

    @Override
    public IsClassBooleanProvider copy() {
        return new IsClassBooleanProvider(classId);
    }

    public IsClassBooleanProvider init(int startTime) {
        return this;
    }
}
