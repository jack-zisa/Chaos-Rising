package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class AngleNumberProvider implements NumberProvider {
    private final VecProvider a;
    private final VecProvider b;

    public AngleNumberProvider(VecProvider a, VecProvider b) {
        this.a = a;
        this.b = b;
    }

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
