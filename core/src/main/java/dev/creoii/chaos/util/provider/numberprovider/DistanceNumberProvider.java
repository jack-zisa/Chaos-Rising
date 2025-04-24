package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class DistanceNumberProvider implements NumberProvider {
    private final VecProvider a;
    private final VecProvider b;

    public DistanceNumberProvider(VecProvider a, VecProvider b) {
        this.a = a;
        this.b = b;
    }

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
