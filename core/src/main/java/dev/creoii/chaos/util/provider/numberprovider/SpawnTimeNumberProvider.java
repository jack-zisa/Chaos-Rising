package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

public record SpawnTimeNumberProvider() implements NumberProvider {
    private static final SpawnTimeNumberProvider INSTANCE = new SpawnTimeNumberProvider();
    public static final MapCodec<SpawnTimeNumberProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.SPAWN_TIME;
    }

    @Override
    public Float get(ContextProvider context) {
        if (context.has(ComponentTypes.ENTITY)) {
            return (float) context.get(ComponentTypes.ENTITY).getSpawnTime();
        }
        return 0f;
    }

    @Override
    public SpawnTimeNumberProvider copy() {
        return new SpawnTimeNumberProvider();
    }

    public SpawnTimeNumberProvider init(int startTime) {
        return this;
    }
}
