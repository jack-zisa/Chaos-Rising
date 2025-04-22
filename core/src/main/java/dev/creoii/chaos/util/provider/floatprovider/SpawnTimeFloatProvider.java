package dev.creoii.chaos.util.provider.floatprovider;

public class SpawnTimeFloatProvider implements FloatProvider {
    @Override
    public Float get(Context context) {
        return (float) context.sourceEntity().getSpawnTime();
    }

    @Override
    public SpawnTimeFloatProvider copy() {
        return new SpawnTimeFloatProvider();
    }

    public SpawnTimeFloatProvider init(int startTime) {
        return this;
    }
}
