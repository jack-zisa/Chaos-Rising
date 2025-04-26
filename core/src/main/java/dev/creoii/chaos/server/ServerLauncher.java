package dev.creoii.chaos.server;

import com.badlogic.gdx.ApplicationAdapter;
import com.esotericsoftware.kryonet.Server;
import dev.creoii.chaos.network.Networking;

import java.io.IOException;

public class ServerLauncher extends ApplicationAdapter {
    private final Server server;

    public ServerLauncher() throws IOException {
        server = new Server();
        server.start();
        server.bind(54555, 54777);

        Networking.register(server.getKryo());
    }

    public Server getServer() {
        return server;
    }
}
