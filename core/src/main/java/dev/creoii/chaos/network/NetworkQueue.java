package dev.creoii.chaos.network;

import com.esotericsoftware.kryonet.Connection;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentLinkedQueue;

public record NetworkQueue<T>(@Nullable Connection connection, ConcurrentLinkedQueue<T> queue) {
    public NetworkQueue(Connection connection) {
        this(connection, new ConcurrentLinkedQueue<>());
    }

    public record QueuedPacket(Connection connection, Object packet) {}
}
