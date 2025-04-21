package dev.creoii.chaos.util.provider.intprovider;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.floatprovider.FloatProvider;

public interface IntProvider extends Provider<Integer> {
    IntProvider copy();

    static IntProvider parse(JsonValue jsonValue) {
        if (jsonValue.isNumber()) {
            return new ConstantIntProvider(jsonValue.asInt());
        }
        String type = jsonValue.getString("type");
        return switch (type) {
            case "binary" -> {
                IntProvider a = IntProvider.parse(jsonValue.get("a"));
                IntProvider b = IntProvider.parse(jsonValue.get("b"));
                BinaryIntProvider.Operation operation = BinaryIntProvider.Operation.valueOf(jsonValue.getString("operation", "ADD").toUpperCase());
                yield new BinaryIntProvider(a, b, operation);
            }
            case "constant" -> {
                int value = jsonValue.getInt("value");
                yield new ConstantIntProvider(value);
            }
            case "curve" -> {
                IntProvider start = IntProvider.parse(jsonValue.get("start"));
                IntProvider end = IntProvider.parse(jsonValue.get("end"));
                FloatProvider duration = FloatProvider.parse(jsonValue.get("duration"));
                CurveIntProvider.CurveType curveType = CurveIntProvider.CurveType.valueOf(jsonValue.getString("curve", "LINEAR").toUpperCase());
                yield new CurveIntProvider(start, end, duration, curveType);
            }
            case "random" -> {
                IntProvider min = IntProvider.parse(jsonValue.get("min"));
                IntProvider max = IntProvider.parse(jsonValue.get("max"));
                yield new RandomIntProvider(min, max);
            }
            case "trig" -> {
                TrigIntProvider.Function function = TrigIntProvider.Function.valueOf(jsonValue.getString("function", "SIN").toUpperCase());
                IntProvider value = IntProvider.parse(jsonValue.get("value"));
                yield new TrigIntProvider(function, value);
            }
            default -> throw new IllegalStateException("Unexpected FloatProvider value: " + type);
        };
    }
}
