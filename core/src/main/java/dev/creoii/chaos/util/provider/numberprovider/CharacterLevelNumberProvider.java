package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

public record CharacterLevelNumberProvider() implements NumberProvider {
    public static final CharacterLevelNumberProvider INSTANCE = new CharacterLevelNumberProvider();
    public static final MapCodec<CharacterLevelNumberProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.CHARACTER_LEVEL;
    }

    @Override
    public Float get(ContextProvider context) {
        if (context.has(ComponentTypes.ENTITY) && context.get(ComponentTypes.ENTITY) instanceof CharacterEntity character) {
            return (float) character.getLevel();
        }
        return -1f;
    }

    @Override
    public CharacterLevelNumberProvider copy() {
        return INSTANCE;
    }

    @Override
    public CharacterLevelNumberProvider init(int startTime) {
        return this;
    }
}
