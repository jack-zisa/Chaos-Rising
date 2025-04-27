package dev.creoii.chaos.util.provider.vecprovider;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import dev.creoii.chaos.util.provider.Operation;
import dev.creoii.chaos.util.provider.Provider;
import dev.creoii.chaos.util.provider.UnaryOperation;
import dev.creoii.chaos.util.provider.booleanprovider.BooleanProvider;
import dev.creoii.chaos.util.provider.numberprovider.ConstantNumberProvider;
import dev.creoii.chaos.util.provider.numberprovider.NumberProvider;

public interface VecProvider extends Provider<Vector2> {
    VecProvider copy();

    static VecProvider parse(JsonValue jsonValue) {
        if (jsonValue.isArray()) {
            NumberProvider a = new ConstantNumberProvider(jsonValue.getFloat(0));
            NumberProvider b = new ConstantNumberProvider(jsonValue.getFloat(1));
            return new ConstantVecProvider(a, b);
        }
        /**
         * TODO:
         * TargetToSource
         * SourceToTarget
         * BulletIndex (corresponding to num provider)
         */
        String type = jsonValue.getString("type");
        return switch (type) {
            case "binary" -> {
                VecProvider a = VecProvider.parse(jsonValue.get("a"));
                VecProvider b = VecProvider.parse(jsonValue.get("b"));
                Operation operation = Operation.valueOf(jsonValue.getString("operation", "ADD").toUpperCase());
                yield new BinaryVecProvider(a, b, operation);
            }
            case "add", "addition" -> {
                VecProvider a = VecProvider.parse(jsonValue.get("a"));
                VecProvider b = VecProvider.parse(jsonValue.get("b"));
                yield new BinaryVecProvider(a, b, Operation.ADD);
            }
            case "sub", "subtract", "subtraction" -> {
                VecProvider a = VecProvider.parse(jsonValue.get("a"));
                VecProvider b = VecProvider.parse(jsonValue.get("b"));
                yield new BinaryVecProvider(a, b, Operation.SUB);
            }
            case "mul", "multiply", "multiplication" -> {
                VecProvider a = VecProvider.parse(jsonValue.get("a"));
                VecProvider b = VecProvider.parse(jsonValue.get("b"));
                yield new BinaryVecProvider(a, b, Operation.MUL);
            }
            case "div", "divide", "division" -> {
                VecProvider a = VecProvider.parse(jsonValue.get("a"));
                VecProvider b = VecProvider.parse(jsonValue.get("b"));
                yield new BinaryVecProvider(a, b, Operation.DIV);
            }
            case "mod", "modulo" -> {
                VecProvider a = VecProvider.parse(jsonValue.get("a"));
                VecProvider b = VecProvider.parse(jsonValue.get("b"));
                yield new BinaryVecProvider(a, b, Operation.MOD);
            }
            case "clamp" -> {
                VecProvider vec = parse(jsonValue.get("vec"));
                NumberProvider minX = jsonValue.has("min_x") ? NumberProvider.parse(jsonValue.get("min_x")) : null;
                NumberProvider minY = jsonValue.has("min_y") ? NumberProvider.parse(jsonValue.get("min_y")) : null;
                NumberProvider maxX = jsonValue.has("max_x") ? NumberProvider.parse(jsonValue.get("max_x")) : null;
                NumberProvider maxY = jsonValue.has("max_y") ? NumberProvider.parse(jsonValue.get("max_y")) : null;
                yield new ClampVecProvider(vec, minX, minY, maxX, maxY);
            }
            case "comparison" -> {
                BooleanProvider comparison = BooleanProvider.parse(jsonValue.get("comparison"));
                VecProvider trueValue = VecProvider.parse(jsonValue.get("min"));
                VecProvider falseValue = VecProvider.parse(jsonValue.get("max"));
                yield new ComparisonVecProvider(comparison, trueValue, falseValue);
            }
            case "constant" -> {
                NumberProvider x = NumberProvider.parse(jsonValue.get("x"));
                NumberProvider y = jsonValue.has("y") ? NumberProvider.parse(jsonValue.get("y")) : null;
                yield new ConstantVecProvider(x, y);
            }
            case "zero" -> new ConstantVecProvider(new Vector2(0f, 0f));
            case "one" -> new ConstantVecProvider(new Vector2(1f, 1f));
            case "direction" -> {
                VecProvider from = VecProvider.parse(jsonValue.get("from"));
                VecProvider to = VecProvider.parse(jsonValue.get("to"));
                yield new DirectionVecProvider(from, to);
            }
            case "mouse_pos" -> new MousePosVecProvider();
            case "normalized" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new NormalizedVecProvider(value);
            }
            case "perpendicular" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new PerpendicularVecProvider(value);
            }
            case "relative_to" -> {
                VecProvider parent = VecProvider.parse(jsonValue.get("parent"));
                VecProvider offset = VecProvider.parse(jsonValue.get("offset"));
                yield new RelativeToVecProvider(parent, offset);
            }
            case "rotate_angle" -> {
                VecProvider direction = VecProvider.parse(jsonValue.get("direction"));
                NumberProvider angle = NumberProvider.parse(jsonValue.get("angle"));
                yield new RotateAngleVecProvider(direction, angle);
            }
            case "rotated_offset" -> {
                VecProvider from = VecProvider.parse(jsonValue.get("from"));
                VecProvider to = VecProvider.parse(jsonValue.get("to"));
                VecProvider offset = VecProvider.parse(jsonValue.get("offset"));
                yield new RotatedOffsetVecProvider(from, to, offset);
            }
            case "source_pos" -> new SourcePosVecProvider();
            case "target_pos" -> new TargetPosVecProvider();
            case "unary" -> {
                UnaryOperation function = UnaryOperation.valueOf(jsonValue.getString("function", "SIN").toUpperCase());
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new UnaryVecProvider(function, value);
            }
            case "sin" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new UnaryVecProvider(UnaryOperation.SIN, value);
            }
            case "cos", "cosine" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new UnaryVecProvider(UnaryOperation.COS, value);
            }
            case "tan", "tangent" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new UnaryVecProvider(UnaryOperation.TAN, value);
            }
            case "sqrt" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new UnaryVecProvider(UnaryOperation.SQRT, value);
            }
            case "cbrt" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new UnaryVecProvider(UnaryOperation.CBRT, value);
            }
            case "abs" -> {
                VecProvider value = VecProvider.parse(jsonValue.get("value"));
                yield new UnaryVecProvider(UnaryOperation.ABS, value);
            }
            default -> throw new IllegalStateException("Unexpected VecProvider value: " + type);
        };
    }
}
