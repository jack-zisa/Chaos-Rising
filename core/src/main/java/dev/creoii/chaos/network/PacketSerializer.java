package dev.creoii.chaos.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PacketSerializer extends Serializer<Object> {
    private final Map<Class<?>, BiConsumer<Output, Object>> WRITE_SCHEMA = new HashMap<>();
    private final Map<Class<?>, Function<Input, ?>> READ_SCHEMA = new HashMap<>();
    public static final PacketSerializer INSTANCE = new PacketSerializer();

    @SuppressWarnings("unchecked")
    public <T> void register(Class<T> clazz, BiConsumer<Output, T> writer, Function<Input, T> reader) {
        WRITE_SCHEMA.put(clazz, (BiConsumer<Output, Object>) writer);
        READ_SCHEMA.put(clazz, reader);
    }

    @Override
    public void write(Kryo kryo, Output output, Object o) {
        BiConsumer<Output, Object> writer = WRITE_SCHEMA.get(o.getClass());
        writer.accept(output, o);
        Networking.LOGGER.info("Write " + o.getClass().getSimpleName() + ": " + Arrays.toString(output.toBytes()), 255);
    }

    @Override
    public Object read(Kryo kryo, Input input, Class<Object> aClass) {
        Function<Input, ?> reader = READ_SCHEMA.get(aClass);
        Networking.LOGGER.info("Read " + aClass.getSimpleName() + ": pos=" + input.position() + " limit=" + input.limit() + " total=" + input.total());
        return reader.apply(input);
    }
}
