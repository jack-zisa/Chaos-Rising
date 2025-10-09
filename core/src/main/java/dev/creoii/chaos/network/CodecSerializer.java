package dev.creoii.chaos.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CodecSerializer extends Serializer<Object> {
    private static final Map<Class<?>, Codec<?>> SCHEMA = new HashMap<>();
    public static final CodecSerializer INSTANCE = new CodecSerializer();

    public static <T> void registerSchema(Class<T> clazz, Codec<T> codec) {
        SCHEMA.put(clazz, codec);
    }

    @SuppressWarnings("unchecked")
    private static <T> Codec<T> getCodec(Class<T> clazz) {
        Codec<?> codec = SCHEMA.get(clazz);
        if (codec == null) {
            throw new IllegalArgumentException("Unknown class type not present in Codec Serialization Schema: " + clazz.getSimpleName());
        }
        return (Codec<T>) codec;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(Kryo kryo, Output output, Object o) {
        Codec<Object> codec = (Codec<Object>) getCodec(o.getClass());
        JsonElement element = codec.encodeStart(JsonOps.INSTANCE, o).getOrThrow();
        byte[] bytes = element.toString().getBytes(StandardCharsets.UTF_8);
        System.out.println("[Network] Write " + o.getClass().getSimpleName() + ": " + element);
        output.writeInt(bytes.length);
        output.writeBytes(bytes);
    }

    @Override
    public Object read(Kryo kryo, Input input, Class<Object> aClass) {
        Codec<?> codec = getCodec(aClass);
        int len = input.readInt();
        byte[] bytes = input.readBytes(len);
        String s = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("[Network] Read " + aClass.getSimpleName() + ": " + s);
        JsonElement element = JsonParser.parseString(s);
        return codec.parse(JsonOps.INSTANCE, element).getOrThrow();
    }
}
