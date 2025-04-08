package dev.creoii.chaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import dev.creoii.chaos.chat.command.CommandManager;
import dev.creoii.chaos.entity.character.CharacterEntity;
import dev.creoii.chaos.texture.TextureManager;

public class Game implements Disposable {
    private final Main main;
    private final TextureManager textureManager;
    private final DataManager dataManager;
    private final TickManager tickManager;
    private final CollisionManager collisionManager;
    private final InputManager inputManager;
    private final CommandManager commandManager;
    private final EntityManager entityManager;
    private int gametime;

    private CharacterEntity activeCharacter;

    public Game(Main main) {
        this.main = main;
        this.textureManager = new TextureManager();
        this.dataManager = new DataManager(main);
        this.tickManager = new TickManager(main);
        this.collisionManager = new CollisionManager(main);
        this.inputManager = new InputManager(main);
        this.commandManager = new CommandManager(main);
        this.entityManager = new EntityManager(main);

        Gdx.input.setInputProcessor(new InputMultiplexer(commandManager, inputManager));
    }

    public void init() {
        textureManager.load();
        dataManager.load();

        activeCharacter = entityManager.addEntity(new CharacterEntity(dataManager.getCharacterClass("wizard")), new Vector2(0, 0));
    }

    public void run(float delta) {
        ++gametime;

        commandManager.update(gametime);
        inputManager.update(gametime);
        tickManager.tick(gametime, delta);
        collisionManager.checkCollisions();
    }

    public Main getMain() {
        return main;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public TickManager getTickManager() {
        return tickManager;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public int getGametime() {
        return gametime;
    }

    public CharacterEntity getActiveCharacter() {
        return activeCharacter;
    }

    @Override
    public void dispose() {
        textureManager.dispose();
    }
}
