package dev.creoii.chaos.server;

import java.io.IOException;

public class ServerLauncher {
    public static void main(String[] args) throws IOException {
        int tcpPort = 54555;
        int udpPort = 54777;

        if (args.length > 0) {
            try {
                tcpPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid TCP port. Using default: " + tcpPort);
            }
        }

        if (args.length > 1) {
            try {
                udpPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid UDP port. Using default: " + udpPort);
            }
        }

        if (tcpPort == udpPort)
            throw new IllegalArgumentException("TCP & UDP ports cannot be the same");

        new ServerGame(tcpPort, udpPort);
    }
}
