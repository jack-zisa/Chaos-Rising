package dev.creoii.chaos.util.provider.numberprovider;

import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.util.provider.Operation;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.TrigFunction;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;
import dev.creoii.chaos.util.provider.vecprovider.VecProvider;
import dev.creoii.chaos.util.stat.Stat;

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
            case "angle" -> {
                VecProvider a = VecProvider.parse(jsonValue.get("a"));
                VecProvider b = VecProvider.parse(jsonValue.get("b"));
                yield new AngleNumberProvider(a, b);
            }
            case "binary" -> {
                NumberProvider a = NumberProvider.parse(jsonValue.get("a"));
                NumberProvider b = NumberProvider.parse(jsonValue.get("b"));
                Operation operation = Operation.valueOf(jsonValue.getString("operation", "ADD").toUpperCase());
                yield new BinaryNumberProvider(a, b, operation);
            }
            case "add", "addition" -> {
                NumberProvider a = NumberProvider.parse(jsonValue.get("a"));
                NumberProvider b = NumberProvider.parse(jsonValue.get("b"));
                yield new BinaryNumberProvider(a, b, Operation.ADD);
            }
            case "sub", "subtract", "subtraction" -> {
                NumberProvider a = NumberProvider.parse(jsonValue.get("a"));
                NumberProvider b = NumberProvider.parse(jsonValue.get("b"));
                yield new BinaryNumberProvider(a, b, Operation.SUB);
            }
            case "mul", "multiply", "multiplication" -> {
                NumberProvider a = NumberProvider.parse(jsonValue.get("a"));
                NumberProvider b = NumberProvider.parse(jsonValue.get("b"));
                yield new BinaryNumberProvider(a, b, Operation.MUL);
            }
            case "div", "divide", "division" -> {
                NumberProvider a = NumberProvider.parse(jsonValue.get("a"));
                NumberProvider b = NumberProvider.parse(jsonValue.get("b"));
                yield new BinaryNumberProvider(a, b, Operation.DIV);
            }
            case "mod", "modulo" -> {
                NumberProvider a = NumberProvider.parse(jsonValue.get("a"));
                NumberProvider b = NumberProvider.parse(jsonValue.get("b"));
                yield new BinaryNumberProvider(a, b, Operation.MOD);
            }
            case "clamp" -> {
                NumberProvider value = NumberProvider.parse(jsonValue.get("value"));
                NumberProvider min = jsonValue.has("min") ? NumberProvider.parse(jsonValue.get("min")) : null;
                NumberProvider max = jsonValue.has("max") ? NumberProvider.parse(jsonValue.get("max")) : null;
                yield new ClampNumberProvider(value, min, max);
            }
            case "comparison" -> {
                BooleanProvider comparison = BooleanProvider.parse(jsonValue.get("comparison"));
                NumberProvider trueValue = NumberProvider.parse(jsonValue.get("min"), 0);
                NumberProvider falseValue = NumberProvider.parse(jsonValue.get("max"), 0);
                yield new ComparisonNumberProvider(comparison, trueValue, falseValue);
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
            case "distance2" -> {
                VecProvider a = VecProvider.parse(jsonValue.get("a"));
                VecProvider b = VecProvider.parse(jsonValue.get("b"));
                yield new Distance2NumberProvider(a, b);
            }
            case "distance" -> {
                VecProvider a = VecProvider.parse(jsonValue.get("a"));
                VecProvider b = VecProvider.parse(jsonValue.get("b"));
                yield new DistanceNumberProvider(a, b);
            }
            case "len2", "length2" -> {
                VecProvider vec = VecProvider.parse(jsonValue.get("vec"));
                yield new Length2NumberProvider(vec);
            }
            case "len", "length" -> {
                VecProvider vec = VecProvider.parse(jsonValue.get("vec"));
                yield new LengthNumberProvider(vec);
            }
            case "bullet_index" -> new BulletIndexNumberProvider();
            case "rand", "random" -> {
                NumberProvider min = NumberProvider.parse(jsonValue.get("min"));
                NumberProvider max = NumberProvider.parse(jsonValue.get("max"));
                yield new RandomNumberProvider(min, max);
            }
            case "spawn_time" -> new SpawnTimeNumberProvider();
            case "stat" -> {
                Stat.Type statType = jsonValue.has("stat") ? Stat.Type.valueOf(jsonValue.getString("stat").toUpperCase()) : Stat.Type.HEALTH;
                yield new StatNumberProvider(statType);
            }
            case "time" -> new TimeNumberProvider();
            case "trig" -> {
                TrigFunction function = TrigFunction.valueOf(jsonValue.getString("function", "SIN").toUpperCase());
                NumberProvider value = NumberProvider.parse(jsonValue.get("value"));
                yield new TrigNumberProvider(function, value);
            }
            case "sin" -> {
                NumberProvider value = NumberProvider.parse(jsonValue.get("value"));
                yield new TrigNumberProvider(TrigFunction.SIN, value);
            }
            case "cos", "cosine" -> {
                NumberProvider value = NumberProvider.parse(jsonValue.get("value"));
                yield new TrigNumberProvider(TrigFunction.COS, value);
            }
            case "tan", "tangent" -> {
                NumberProvider value = NumberProvider.parse(jsonValue.get("value"));
                yield new TrigNumberProvider(TrigFunction.TAN, value);
            }
            default -> throw new IllegalStateException("Unexpected FloatProvider value: " + type);
        };
    }
}
