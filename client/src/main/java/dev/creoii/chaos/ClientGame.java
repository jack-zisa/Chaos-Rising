package dev.creoii.chaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.esotericsoftware.kryonet.Client;
import dev.creoii.chaos.chat.command.CommandManager;
import dev.creoii.chaos.entity.ClientCharacterEntity;
import dev.creoii.chaos.network.Networking;
import dev.creoii.chaos.texture.TextureManager;

import java.io.IOException;

public class ClientGame implements Game {
    private final Client client;
    private final ClientMain main;
    private final TextureManager textureManager;
    private final OptionsManager optionsManager;
    private final InputManager inputManager;
    private final CommandManager commandManager;
    private ClientCharacterEntity character;

    public ClientGame(ClientMain main) throws IOException {
        client = new Client();
        Networking.register(client.getKryo());
        client.addListener(new ClientListener(this));
        client.start();
        client.connect(5000, "localhost", 54555, 54777);

        this.main = main;
        textureManager = new TextureManager();
        optionsManager = new OptionsManager();
        inputManager = new InputManager(main);
        commandManager = new CommandManager(main);

        Gdx.input.setInputProcessor(new InputMultiplexer(commandManager, inputManager));
    }

    public void run(float delta) {
        commandManager.update();
        inputManager.update();
    }

    public Client getClient() {
        return client;
    }

    public ClientMain getMain() {
        return main;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    @Override
    public OptionsManager getOptionsManager() {
        return optionsManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ClientCharacterEntity getCharacter() {
        return character;
    }

    public void setCharacter(ClientCharacterEntity character) {
        this.character = character;
    }

    @Override
    public int getGametime() {
        return 0;
    }
}
