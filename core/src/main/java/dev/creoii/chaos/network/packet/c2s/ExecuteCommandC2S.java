package dev.creoii.chaos.network.packet.c2s;

import java.util.UUID;

public record ExecuteCommandC2S(UUID uuid, String commandType, String[] args) {
}
