package dev.creoii.chaos.util.provider.numberprovider;

import com.mojang.serialization.MapCodec;
import dev.creoii.chaos.util.context.ComponentTypes;
import dev.creoii.chaos.util.context.ContextProvider;

public record RoomDepthNumberProvider() implements NumberProvider {
    public static final RoomDepthNumberProvider INSTANCE = new RoomDepthNumberProvider();
    public static final MapCodec<RoomDepthNumberProvider> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Type getType() {
        return Type.ROOM_DEPTH;
    }

    @Override
    public Float get(ContextProvider context) {
        if (context.has(ComponentTypes.ROOM_DEPTH)) {
            return (float) context.get(ComponentTypes.ROOM_DEPTH);
        }
        return -1f;
    }

    @Override
    public RoomDepthNumberProvider copy() {
        return INSTANCE;
    }

    @Override
    public RoomDepthNumberProvider init(int startTime) {
        return this;
    }
}
