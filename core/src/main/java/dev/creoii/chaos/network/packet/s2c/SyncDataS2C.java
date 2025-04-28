package dev.creoii.chaos.network.packet.s2c;

import java.io.Serializable;

public record SyncDataS2C(byte[] data) implements Serializable {
}
