package dev.creoii.chaos.network.packet.c2s;

import java.io.Serializable;
import java.util.UUID;

public record ExecuteCommandC2S(UUID uuid, String commandType, String[] args) implements Serializable {
}
