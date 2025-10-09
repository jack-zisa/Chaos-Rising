package dev.creoii.chaos.util.provider.numberprovider;

public record SpawnTimeNumberProvider() implements NumberProvider {
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
