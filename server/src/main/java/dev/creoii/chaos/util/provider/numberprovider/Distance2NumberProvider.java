package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class Distance2NumberProvider implements NumberProvider {
    private final VecProvider a;
    private final VecProvider b;

    public Distance2NumberProvider(VecProvider a, VecProvider b) {
        this.a = a;
        this.b = b;
    }

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
