package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class Length2NumberProvider implements NumberProvider {
    private final VecProvider vec;

    public Length2NumberProvider(VecProvider vec) {
        this.vec = vec;
    }

    @Override
    public Float get(Context context) {
        return vec.get(context).len2();
    }

    @Override
    public Length2NumberProvider copy() {
        return new Length2NumberProvider(vec.copy());
    }

    public Length2NumberProvider init(int startTime) {
        return this;
    }
}
