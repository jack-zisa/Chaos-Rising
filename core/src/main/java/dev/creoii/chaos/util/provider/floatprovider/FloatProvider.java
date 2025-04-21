package dev.creoii.chaos.util.provider.floatprovider;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.util.provider.Provider;

public interface FloatProvider extends Provider<Float> {
    FloatProvider copy();

    FloatProvider init(int startTime);

    static FloatProvider parse(JsonValue jsonValue, float defaultValue) {
        if (jsonValue == null) {
            return new ConstantFloatProvider(defaultValue);
        }

        return jsonValue.isNumber() || jsonValue.isObject() ? parse(jsonValue) : new ConstantFloatProvider(defaultValue);
    }

    static FloatProvider parse(JsonValue jsonValue) {
        if (jsonValue.isNumber()) {
            return new ConstantFloatProvider(jsonValue.asFloat());
        }
        String type = jsonValue.getString("type");
        return switch (type) {
            case "binary" -> {
                FloatProvider a = FloatProvider.parse(jsonValue.get("a"));
                FloatProvider b = FloatProvider.parse(jsonValue.get("b"));
                BinaryFloatProvider.Operation operation = BinaryFloatProvider.Operation.valueOf(jsonValue.getString("operation", "ADD").toUpperCase());
                yield new BinaryFloatProvider(a, b, operation);
            }
            case "constant" -> {
                float value = jsonValue.getFloat("value");
                yield new ConstantFloatProvider(value);
            }
            case "curve" -> {
                FloatProvider start = FloatProvider.parse(jsonValue.get("start"));
                FloatProvider end = FloatProvider.parse(jsonValue.get("end"));
                FloatProvider duration = FloatProvider.parse(jsonValue.get("duration"));
                CurveFloatProvider.CurveType curveType = CurveFloatProvider.CurveType.valueOf(jsonValue.getString("curve", "LINEAR").toUpperCase());
                yield new CurveFloatProvider(start, end, duration, curveType);
            }
            case "cycle" -> {
                FloatProvider value = FloatProvider.parse(jsonValue.get("value"));
                FloatProvider max = FloatProvider.parse(jsonValue.get("max"));
                yield new CycleFloatProvider(value, max);
            }
            case "random" -> {
                FloatProvider min = FloatProvider.parse(jsonValue.get("min"));
                FloatProvider max = FloatProvider.parse(jsonValue.get("max"));
                yield new RandomFloatProvider(min, max);
            }
            case "time" -> new TimeFloatProvider();
            case "trig" -> {
                TrigFloatProvider.Function function = TrigFloatProvider.Function.valueOf(jsonValue.getString("function", "SIN").toUpperCase());
                FloatProvider value = FloatProvider.parse(jsonValue.get("value"));
                yield new TrigFloatProvider(function, value);
            }
            default -> throw new IllegalStateException("Unexpected FloatProvider value: " + type);
        };
    }
}
