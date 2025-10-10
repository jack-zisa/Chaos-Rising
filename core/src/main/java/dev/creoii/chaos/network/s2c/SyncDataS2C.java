package dev.creoii.chaos.network.s2c;

import com.mojang.serialization.Codec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record SyncDataS2C(byte[] data) implements Serializable {
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
}
