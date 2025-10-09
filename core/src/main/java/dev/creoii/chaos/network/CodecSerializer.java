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

public class CodecSerializer<T> extends Serializer<T> {
    private final Codec<T> codec;

    public CodecSerializer(Codec<T> codec) {
        this.codec = codec;
    }

    @Override
    public void write(Kryo kryo, Output output, T o) {
        JsonElement element = codec.encodeStart(JsonOps.INSTANCE, o).getOrThrow();
        byte[] bytes = element.toString().getBytes(StandardCharsets.UTF_8);
        System.out.println("write: " + element);
        output.writeInt(bytes.length);
        output.writeBytes(bytes);
    }

    @Override
    public T read(Kryo kryo, Input input, Class<T> aClass) {
        int len = input.readInt();
        byte[] bytes = input.readBytes(len);
        String s = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("read: " + s);
        JsonElement element = JsonParser.parseString(s);
        return codec.parse(JsonOps.INSTANCE, element).getOrThrow();
    }
}
