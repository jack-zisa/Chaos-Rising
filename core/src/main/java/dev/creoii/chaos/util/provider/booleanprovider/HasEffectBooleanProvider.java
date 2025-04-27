package dev.creoii.chaos.util.provider.booleanprovider;

import dev.creoii.chaos.entity.ServerLivingEntity;

public class HasEffectBooleanProvider implements BooleanProvider {
    private final String effect;

    public HasEffectBooleanProvider(String effect) {
        this.effect = effect;
    }

    @Override
    public Boolean get(Context context) {
        if (context.sourceEntity() instanceof ServerLivingEntity livingEntity) {
            return livingEntity.hasStatusEffect(effect);
        }
        return false;
    }

    @Override
    public HasEffectBooleanProvider copy() {
        return new HasEffectBooleanProvider(effect);
    }

    public HasEffectBooleanProvider init(int startTime) {
        return this;
    }
}
