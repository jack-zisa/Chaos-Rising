package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

public record DungeonDepthNumberProvider() implements NumberProvider {
    public static final DungeonDepthNumberProvider INSTANCE = new DungeonDepthNumberProvider();
    public static final MapCodec<DungeonDepthNumberProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.DUNGEON_DEPTH;
    }

    @Override
    public Float get(ContextProvider context) {
        if (context.has(ComponentTypes.DUNGEON)) {
            return (float) context.get(ComponentTypes.DUNGEON).getDepth();
        }
        return -1f;
    }

    @Override
    public DungeonDepthNumberProvider copy() {
        return INSTANCE;
    }

    @Override
    public DungeonDepthNumberProvider init(int startTime) {
        return this;
    }
}
