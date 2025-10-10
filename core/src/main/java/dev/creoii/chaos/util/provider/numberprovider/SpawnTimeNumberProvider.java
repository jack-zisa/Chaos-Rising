package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;

public record SpawnTimeNumberProvider() implements NumberProvider {
    private static final SpawnTimeNumberProvider INSTANCE = new SpawnTimeNumberProvider();
    public static final MapCodec<SpawnTimeNumberProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.SPAWN_TIME;
    }

    @Override
    public Float get(Context context) {
        return (float) context.sourceEntity().getSpawnTime();
    }

    @Override
    public SpawnTimeNumberProvider copy() {
        return new SpawnTimeNumberProvider();
    }

    public SpawnTimeNumberProvider init(int startTime) {
        return this;
    }
}
