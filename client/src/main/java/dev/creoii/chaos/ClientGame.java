package dev.creoii.chaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import dev.creoii.chaos.chat.command.CommandManager;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.texture.TextureManager;

public class ClientGame {
    private final Main main;
    private final TextureManager textureManager;
    private final OptionsManager optionsManager;
    private final InputManager inputManager;
    private final CommandManager commandManager;
    private CharacterEntity character;

    public ClientGame(Main main) {
        this.main = main;
        textureManager = new TextureManager(main);
        optionsManager = new OptionsManager();
        inputManager = new InputManager(main);
        commandManager = new CommandManager(main);

        Gdx.input.setInputProcessor(new InputMultiplexer(commandManager, inputManager));
    }

    public void run(float delta) {
        commandManager.update();
        inputManager.update();
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

    public CharacterEntity getCharacter() {
        return character;
    }
}
