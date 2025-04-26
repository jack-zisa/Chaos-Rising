package dev.creoii.chaos.util.provider.booleanprovider;

import dev.creoii.chaos.entity.CharacterEntityType;
import dev.creoii.chaos.entity.character.CharacterEntity;

public class IsClassBooleanProvider implements BooleanProvider {
    private final String classId;

    public IsClassBooleanProvider(String classId) {
        this.classId = classId;
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
