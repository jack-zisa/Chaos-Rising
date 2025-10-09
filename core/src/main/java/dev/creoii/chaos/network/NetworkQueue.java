package dev.creoii.chaos.network;

import com.esotericsoftware.kryonet.Connection;

import java.util.concurrent.ConcurrentLinkedQueue;

public record NetworkQueue<T>(Connection connection, ConcurrentLinkedQueue<T> queue) {
    public NetworkQueue(Connection connection) {
        this(connection, new ConcurrentLinkedQueue<>());
    }

    public record QueuedPacket(Connection connection, Object packet) {}
}
