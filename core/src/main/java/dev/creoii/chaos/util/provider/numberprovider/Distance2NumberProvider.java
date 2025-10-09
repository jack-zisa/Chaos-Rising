package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record Distance2NumberProvider(VecProvider a, VecProvider b) implements NumberProvider {

    @Override
    public Float get(Context context) {
        return a.get(context).dst2(b.get(context));
    }

    @Override
    public Distance2NumberProvider copy() {
        return new Distance2NumberProvider(a.copy(), b.copy());
    }

    public Distance2NumberProvider init(int startTime) {
        return this;
    }
}
