package dev.creoii.chaos.util.provider.booleanprovider;

import dev.creoii.chaos.entity.CharacterEntity;
import dev.creoii.chaos.entity.CharacterEntityType;

public record IsClassBooleanProvider(String classId) implements BooleanProvider {
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
