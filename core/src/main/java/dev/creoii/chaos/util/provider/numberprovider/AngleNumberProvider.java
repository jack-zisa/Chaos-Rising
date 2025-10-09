package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record AngleNumberProvider(VecProvider a, VecProvider b) implements NumberProvider {
    @Override
    public Float get(Context context) {
        return a.get(context).angleDeg(b.get(context));
    }

    @Override
    public AngleNumberProvider copy() {
        return new AngleNumberProvider(a.copy(), b.copy());
    }

    public AngleNumberProvider init(int startTime) {
        return this;
    }
}
