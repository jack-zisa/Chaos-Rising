package dev.creoii.chaos.util.provider;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.util.TriFunction;

import javax.annotation.Nullable;

public class IntProvider extends Number implements Provider<Integer> {
    private final TriFunction<Game, Integer, JsonValue, Integer> provider;
    private JsonValue data;
    private int value;

    protected IntProvider(TriFunction<Game, Integer, JsonValue, Integer> provider) {
        this.provider = provider;
    }

    static IntProvider register(String id, TriFunction<Game, Integer, JsonValue, Integer> provider) {
        return IntProviders.ALL.put(id, new IntProvider(provider));
    }

    @Nullable
    public static IntProvider parse(JsonValue jsonValue) {
        if (jsonValue.isNumber()) {
            return IntProvider.create(IntProviders.CONSTANT, jsonValue.asInt(), jsonValue);
        } else if (jsonValue.isObject()) {
            String id = jsonValue.getString("id", "constant");
            int value = jsonValue.getInt("value", 0);
            return IntProvider.create(IntProviders.ALL.get(id), value, jsonValue);
        }
        return null;
    }

    public static IntProvider create(IntProvider template, int startValue, JsonValue data) {
        template.value = startValue;
        template.data = data;
        return template;
    }

    public static IntProvider create(IntProvider template, int startValue) {
        template.value = startValue;
        return template;
    }

    public static IntProvider create(IntProvider template) {
        return template;
    }

    @Override
    public Provider<Integer> copy() {
        return create(new IntProvider(provider), value, data);
    }

    @Override
    public Integer get(Game game) {
        return value = provider.apply(game, value, data);
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }
}
