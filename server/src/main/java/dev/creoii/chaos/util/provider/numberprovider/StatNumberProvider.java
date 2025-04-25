package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.util.stat.Stat;

public class StatNumberProvider implements NumberProvider {
    private final Stat.Type statType;

    public StatNumberProvider(Stat.Type statType) {
        this.statType = statType;
    }

    @Override
    public Float get(Context context) {
        if (context.sourceEntity() instanceof LivingEntity livingEntity) {
            switch (statType) {
                case HEALTH -> livingEntity.getStats().health.value();
                case ATTACK_SPEED -> livingEntity.getStats().attackSpeed.value();
                case SPEED -> livingEntity.getStats().speed.value();
                case ATTACK -> livingEntity.getStats().attack.value();
                case DEFENSE -> livingEntity.getStats().defense.value();
                case VITALITY -> livingEntity.getStats().vitality.value();
            }
        }
        return 0f;
    }

    @Override
    public StatNumberProvider copy() {
        return new StatNumberProvider(statType);
    }

    public StatNumberProvider init(int startTime) {
        return this;
    }
}
