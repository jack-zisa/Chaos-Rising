package dev.creoii.chaos.util.provider.booleanprovider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.CharacterEntityType;

public record IsClassBooleanProvider(String classId) implements BooleanProvider {
    public static final MapCodec<HasEffectBooleanProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Codec.STRING.fieldOf("classId").forGetter(HasEffectBooleanProvider::classId)
        ).apply(instance, HasEffectBooleanProvider::new);
    });

    @Override
    public Type getType() {
        return Type.HAS_EFFECT;
    }

    @Override
    public Boolean get(Context context) {
        if (context.sourceEntity() instanceof CharacterEntity character) {
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
