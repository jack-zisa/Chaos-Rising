package dev.creoii.chaos.util.provider.colorprovider;

import com.badlogic.gdx.graphics.Color;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.chaos.util.context.ContextProvider;

import java.util.List;

public record ConstantColorProvider(Color color) implements ColorProvider {
    public static final MapCodec<ConstantColorProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.either(Codec.INT, Codec.either(Codec.STRING, Codec.INT.listOf(3, 4))).fieldOf("value").forGetter(provider -> Either.left(provider.color.toIntBits()))
        ).apply(instance, either -> {
            if (either.left().isPresent()) {
                return new ConstantColorProvider(new Color(either.left().get()));
            } else {
                Either<String, List<Integer>> sub = either.right().get();
                if (sub.left().isPresent()) {
                    return new ConstantColorProvider(parseColor(sub.left().get()));
                } else {
                    List<Integer> list = sub.right().get();
                    return list.size() == 3 ? new ConstantColorProvider(new Color(list.getFirst(), list.get(1), list.get(2), 0f)) : new ConstantColorProvider(new Color(list.getFirst(), list.get(1), list.get(2), list.get(3)));
                }
            }
        })
    );

    @Override
    public Type getType() {
        return Type.CONSTANT;
    }

    @Override
    public Color get(ContextProvider context) {
        return color;
    }

    private static Color parseColor(String s) {
        try {
            if (s.startsWith("#")) {
                return Color.valueOf(s);
            }
            return switch (s.toLowerCase()) {
                case "white" -> Color.WHITE;
                case "black" -> Color.BLACK;
                case "red" -> Color.RED;
                case "green" -> Color.GREEN;
                case "blue" -> Color.BLUE;
                case "yellow" -> Color.YELLOW;
                case "clear" -> Color.CLEAR;
                case "navy" -> Color.NAVY;
                case "royal" -> Color.ROYAL;
                case "slate" -> Color.SLATE;
                case "sky" -> Color.SKY;
                case "cyan" -> Color.CYAN;
                case "teal" -> Color.TEAL;
                case "chartreuse" -> Color.CHARTREUSE;
                case "lime" -> Color.LIME;
                case "forest" -> Color.FOREST;
                case "olive" -> Color.OLIVE;
                case "gold" -> Color.GOLD;
                case "goldenrod" -> Color.GOLDENROD;
                case "orange" -> Color.ORANGE;
                case "brown" -> Color.BROWN;
                case "tan" -> Color.TAN;
                case "firebrick" -> Color.FIREBRICK;
                case "scarlet" -> Color.SCARLET;
                case "coral" -> Color.CORAL;
                case "salmon" -> Color.SALMON;
                case "pink" -> Color.PINK;
                case "magenta" -> Color.MAGENTA;
                case "purple" -> Color.PURPLE;
                case "violet" -> Color.VIOLET;
                case "maroon" -> Color.MAROON;
                case "gray", "grey" -> Color.GRAY;
                default -> throw new IllegalArgumentException("Unknown color name: " + s);
            };
        } catch (Exception e) {
            throw new RuntimeException("Invalid color value: " + s, e);
        }
    }
}
