package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.entity.LivingEntity;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;
import dev.creoii.chaos.util.stat.Stat;

public record StatNumberProvider(Stat.Type statType) implements NumberProvider {
    public static final MapCodec<StatNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
            Stat.Type.CODEC.fieldOf("stat").orElse(Stat.Type.HEALTH).forGetter(StatNumberProvider::statType)
        ).apply(instance, StatNumberProvider::new);
    });

    @Override
    public Type getType() {
        return Type.STAT;
    }

    @Override
    public Float get(ContextProvider context) {
        if (context.get(ComponentTypes.ENTITY) instanceof LivingEntity livingEntity) {
            switch (statType) {
                case HEALTH -> livingEntity.getStats().health().value();
                case ATTACK_SPEED -> livingEntity.getStats().attackSpeed().value();
                case SPEED -> livingEntity.getStats().speed().value();
                case ATTACK -> livingEntity.getStats().attack().value();
                case DEFENSE -> livingEntity.getStats().defense().value();
                case VITALITY -> livingEntity.getStats().vitality().value();
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
