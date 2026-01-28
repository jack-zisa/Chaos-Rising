package dev.creoii.chaos.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import dev.creoii.chaos.DataManager;
import dev.creoii.chaos.network.NetworkQueue;
import dev.creoii.chaos.network.c2s.*;
import dev.creoii.chaos.network.s2c.*;
import dev.creoii.chaos.inventory.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ServerListener extends Listener {
    private final ServerGame game;

    public ServerListener(ServerGame game) {
        this.game = game;
    }

    @Override
    public void connected(Connection connection) {
        ServerGame.LOGGER.info("Client connected: " + connection.getRemoteAddressTCP());

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource("data/");
        if (url == null)
            throw new IllegalStateException("Could not find data folder");
        Path dataRoot;
        try {
            dataRoot = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        for (DataManager.SchemaType schemaType : DataManager.SchemaType.values()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(32768);
            try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {
                Path schemaRoot = dataRoot.resolve(schemaType.getPath());
                try (Stream<Path> paths = Files.walk(schemaRoot)) {
                    for (Path path : (Iterable<Path>) paths.filter(Files::isRegularFile)::iterator) {
                        ZipEntry entry = new ZipEntry(
                            schemaRoot.relativize(path).toString().replace("\\", "/")
                        );
                        zipOut.putNextEntry(entry);
                        Files.copy(path, zipOut);
                        zipOut.closeEntry();
                    }
                }
                zipOut.finish();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            game.getServer().sendToTCP(connection.getID(), new SyncDataS2C(baos.toByteArray()));
        }

        game.getServer().sendToTCP(connection.getID(), new LoadDataS2C());
    }

    @Override
    public void received(Connection connection, Object object) {
        game.networkQueue.queue().add(new NetworkQueue.QueuedPacket(connection, object));
    }

    public void handlePacket(Connection connection, Object object) {
    }

    @Override
    public void disconnected(Connection connection) {
        ServerGame.LOGGER.info("Client disconnected: " + connection.getID());
    }
}
