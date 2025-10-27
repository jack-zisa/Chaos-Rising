package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;

public record RemoveEntitiesS2C(List<Integer> ids) {
    public static final Codec<RemoveEntitiesS2C> CODEC = Codec.INT.listOf().xmap(RemoveEntitiesS2C::new, RemoveEntitiesS2C::ids);

    public static void write(Output output, RemoveEntitiesS2C o) {
        int size = o.ids.size();
        output.writeInt(size);
        for (int i = 0; i < size; ++i) {
            output.writeInt(o.ids.get(i));
        }
    }

    public static RemoveEntitiesS2C read(Input input) {
        List<Integer> ids = new ArrayList<>();
        int size = input.readInt();
        for (int i = 0; i < size; ++i) {
            ids.add(input.readInt());
        }
        return new RemoveEntitiesS2C(ids);
    }
}
