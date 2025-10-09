package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record DistanceNumberProvider(VecProvider a, VecProvider b) implements NumberProvider {

    @Override
    public Float get(Context context) {
        return a.get(context).dst(b.get(context));
    }

    @Override
    public DistanceNumberProvider copy() {
        return new DistanceNumberProvider(a.copy(), b.copy());
    }

    public DistanceNumberProvider init(int startTime) {
        return this;
    }
}
