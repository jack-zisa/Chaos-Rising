package dev.creoii.chaos.network.s2c;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.mojang.serialization.Codec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public record SyncDataS2C(byte[] data) {
    public static final Codec<SyncDataS2C> CODEC = Codec.BYTE.listOf().xmap(bytes -> {
        byte[] byteArr = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); ++i) {
            byteArr[i] = bytes.get(i);
        }
        return new SyncDataS2C(byteArr);
    }, syncDataS2C -> {
        List<Byte> bytes = new ArrayList<>();
        for (byte b : syncDataS2C.data) {
            bytes.add(b);
        }
        return bytes;
    });

    public static void write(Output output, SyncDataS2C o) {
        output.writeBytes(o.data);
    }

    public static SyncDataS2C read(Input input) {
        byte[] bytes;
        try {
            bytes = input.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new SyncDataS2C(bytes);
    }
}
