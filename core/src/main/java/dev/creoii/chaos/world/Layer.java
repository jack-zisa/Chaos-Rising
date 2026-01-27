package dev.creoii.chaos.world;

import com.mojang.serialization.Codec;

import java.util.List;

public record Layer(List<List<String>> tiles) {
    public static final Codec<Layer> CODEC = Codec.list(Codec.list(Codec.STRING)).xmap(Layer::new, Layer::tiles);
}
