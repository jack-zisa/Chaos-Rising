package dev.creoii.chaos.util.provider.numberprovider;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.util.provider.Operation;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.TrigFunction;

public interface NumberProvider extends Provider<Float> {
    NumberProvider copy();

    NumberProvider init(int startTime);

    default int getInt(Context context) {
        return Math.round(get(context));
    }

    static NumberProvider parse(JsonValue jsonValue, float defaultValue) {
        if (jsonValue == null) {
            return new ConstantNumberProvider(defaultValue);
        }

        return jsonValue.isNumber() || jsonValue.isObject() ? parse(jsonValue) : new ConstantNumberProvider(defaultValue);
    }

    static NumberProvider parse(JsonValue jsonValue) {
        if (jsonValue.isNumber()) {
            return new ConstantNumberProvider(jsonValue.asFloat());
        }
        String type = jsonValue.getString("type");
        return switch (type) {
            case "binary" -> {
                NumberProvider a = NumberProvider.parse(jsonValue.get("a"));
                NumberProvider b = NumberProvider.parse(jsonValue.get("b"));
                Operation operation = Operation.valueOf(jsonValue.getString("operation", "ADD").toUpperCase());
                yield new BinaryNumberProvider(a, b, operation);
            }
            case "constant" -> {
                float value = jsonValue.getFloat("value");
                yield new ConstantNumberProvider(value);
            }
            case "curve" -> {
                NumberProvider start = NumberProvider.parse(jsonValue.get("start"));
                NumberProvider end = NumberProvider.parse(jsonValue.get("end"));
                NumberProvider duration = NumberProvider.parse(jsonValue.get("duration"));
                CurveNumberProvider.CurveType curveType = CurveNumberProvider.CurveType.valueOf(jsonValue.getString("curve", "LINEAR").toUpperCase());
                yield new CurveNumberProvider(start, end, duration, curveType);
            }
            case "cycle" -> {
                NumberProvider value = NumberProvider.parse(jsonValue.get("value"));
                NumberProvider max = NumberProvider.parse(jsonValue.get("max"));
                yield new CycleNumberProvider(value, max);
            }
            case "bullet_index" -> new BulletIndexNumberProvider();
            case "random" -> {
                NumberProvider min = NumberProvider.parse(jsonValue.get("min"));
                NumberProvider max = NumberProvider.parse(jsonValue.get("max"));
                yield new RandomNumberProvider(min, max);
            }
            case "spawn_time" -> new SpawnTimeNumberProvider();
            case "time" -> new TimeNumberProvider();
            case "trig" -> {
                TrigFunction function = TrigFunction.valueOf(jsonValue.getString("function", "SIN").toUpperCase());
                NumberProvider value = NumberProvider.parse(jsonValue.get("value"));
                yield new TrigNumberProvider(function, value);
            }
            default -> throw new IllegalStateException("Unexpected FloatProvider value: " + type);
        };
    }
}
