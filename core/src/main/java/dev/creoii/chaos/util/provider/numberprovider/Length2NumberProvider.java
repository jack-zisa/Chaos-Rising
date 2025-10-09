package dev.creoii.chaos.util.provider.numberprovider;

import dev.creoii.chaos.util.provider.vecprovider.VecProvider;

public record Length2NumberProvider(VecProvider vec) implements NumberProvider {

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
