package dev.creoii.chaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.esotericsoftware.kryonet.Client;
import dev.creoii.chaos.chat.command.CommandManager;
import dev.creoii.chaos.entity.Entity;
import dev.creoii.chaos.network.Networking;
import dev.creoii.chaos.texture.TextureManager;

import java.io.IOException;

public class ClientGame {
    private final Client client;
    private final Main main;
    private final TextureManager textureManager;
    private final OptionsManager optionsManager;
    private final InputManager inputManager;
    private final CommandManager commandManager;
    private Entity character;

    public ClientGame(Main main) throws IOException {
        client = new Client();
        client.start();
        client.connect(5000, "localhost", 54555, 54777);

        Networking.register(client.getKryo());

        this.main = main;
        textureManager = new TextureManager(main);
        optionsManager = new OptionsManager();
        inputManager = new InputManager(main);
        commandManager = new CommandManager(main);

        Gdx.input.setInputProcessor(new InputMultiplexer(commandManager, inputManager));

        client.addListener(new ClientListener(main.getGame()));
    }

    public void run(float delta) {
        commandManager.update();
        inputManager.update();
    }

    public Client getClient() {
        return client;
    }

    public Main getMain() {
        return main;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public OptionsManager getOptionsManager() {
        return optionsManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Entity getCharacter() {
        return character;
    }
}
