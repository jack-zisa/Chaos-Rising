package dev.creoii.chaos.util.provider.booleanprovider;


public record NotBooleanProvider(BooleanProvider value) implements BooleanProvider {
    @Override
    public Boolean get(Context context) {
        return !value.get(context);
    }

    @Override
    public NotBooleanProvider copy() {
        return new NotBooleanProvider(value.copy());
    }

    public NotBooleanProvider init(int startTime) {
        value.init(startTime);
        return this;
    }
}
