package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public class LengthNumberProvider implements NumberProvider {
    private final VecProvider vec;

    public LengthNumberProvider(VecProvider vec) {
        this.vec = vec;
    }

    @Override
    public Float get(Context context) {
        return vec.get(context).len();
    }

    @Override
    public LengthNumberProvider copy() {
        return new LengthNumberProvider(vec.copy());
    }

    public LengthNumberProvider init(int startTime) {
        return this;
    }
}
