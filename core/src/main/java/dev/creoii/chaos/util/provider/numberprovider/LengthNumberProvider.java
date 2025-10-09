package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record LengthNumberProvider(VecProvider vec) implements NumberProvider {

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
