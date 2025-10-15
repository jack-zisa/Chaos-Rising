package dev.creoii.chaos.publish;

import com.badlogicgames.packr.Packr;
import com.badlogicgames.packr.PackrConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Publish {
    public static void main(String[] args) throws IOException {
        PackrConfig config = new PackrConfig();
        config.platform = PackrConfig.Platform.Windows64; // use 64 if you can
        config.jdk = "C:/Users/jackr/Downloads/jdk-25_windows-x64_bin.zip"; // your downloaded JRE/JDK zip
        config.executable = "chaos-rising-0.0.1-client";
        config.classpath = List.of("./lwjgl3/build/libs/Chaos-Rising-0.0.1.jar");
        config.mainClass = "dev.creoii.chaos.lwjgl3.ClientLauncher";
        config.vmArgs = List.of("-Xmx1G");
        config.minimizeJre = "soft"; // or path to JSON
        config.outDir = new File("out/windows");
        config.useZgcIfSupportedOs = true;
        new Packr().pack(config);
    }
}
