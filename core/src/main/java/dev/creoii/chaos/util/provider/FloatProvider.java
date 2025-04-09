package dev.creoii.chaos.util.provider;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.Game;
import dev.creoii.chaos.util.function.TriFunction;

public class FloatProvider extends Number implements Provider<Float> {
    public static final Provider<Float> DEFAULT = FloatProvider.create(FloatProviders.ALL.get("constant"));
    private final TriFunction<Game, Float, JsonValue, Float> provider;
    private JsonValue data;
    private float value;

    protected FloatProvider(TriFunction<Game, Float, JsonValue, Float> provider) {
        this.provider = provider;
    }

    static FloatProvider register(String id, TriFunction<Game, Float, JsonValue, Float> provider) {
        return FloatProviders.ALL.put(id, new FloatProvider(provider));
    }

    public static Provider<Float> parse(JsonValue jsonValue) {
        return parse(jsonValue, 0f);
    }

    public static Provider<Float> parse(JsonValue jsonValue, Float defaultValue) {
        if (jsonValue == null) {
            return DEFAULT.copy();
        }

        if (jsonValue.isNumber()) {
            return FloatProvider.create(FloatProviders.ALL.get("constant").copy(), jsonValue.asFloat(), jsonValue);
        } else if (jsonValue.isObject()) {
            String id = jsonValue.getString("id", "constant");
            float value = jsonValue.getFloat("value", defaultValue);
            return FloatProvider.create(FloatProviders.ALL.get(id).copy(), value, jsonValue);
        }
        return DEFAULT.copy();
    }

    public static FloatProvider create(FloatProvider template, float startValue, JsonValue data) {
        template.value = startValue;
        template.data = data;
        return template;
    }

    public static FloatProvider create(FloatProvider template, float startValue) {
        template.value = startValue;
        return template;
    }

    public static FloatProvider create(FloatProvider template) {
        template.value = 0f;
        return template;
    }

    @Override
    public FloatProvider copy() {
        return create(new FloatProvider(provider), value, data);
    }

    @Override
    public Float get(Game game) {
        return value = provider.apply(game, value, data);
    }

    @Override
    public int intValue() {
        return Math.round(value);
    }

    @Override
    public long longValue() {
        return Math.round(value);
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
