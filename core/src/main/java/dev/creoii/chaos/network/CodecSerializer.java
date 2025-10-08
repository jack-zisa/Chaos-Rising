package dev.creoii.chaos.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

public class CodecSerializer<T> extends Serializer<T> {
    private final Codec<T> codec;

    public CodecSerializer(Codec<T> codec) {
        this.codec = codec;
    }

    @Override
    public void write(Kryo kryo, Output output, T o) {
        JsonElement element = codec.encodeStart(JsonOps.INSTANCE, o).getOrThrow();
        System.out.println("write: " + element.toString());
        output.writeString(element.toString());
    }

    @Override
    public T read(Kryo kryo, Input input, Class<T> aClass) {
        String s = input.readString();
        JsonElement element = JsonParser.parseString(s);
        System.out.println("read: " + s);
        return codec.parse(JsonOps.INSTANCE, element).getOrThrow();
    }
}
