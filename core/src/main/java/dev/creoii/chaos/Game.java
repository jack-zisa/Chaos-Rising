package dev.creoii.chaos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import dev.creoii.chaos.chat.command.CommandManager;
import dev.creoii.chaos.entity.EnemyEntity;
import dev.creoii.chaos.entity.character.CharacterEntity;

public class Game {
    private final Main main;
    private final DataManager dataManager;
    private final TickManager tickManager;
    private final InputManager inputManager;
    private final CommandManager commandManager;
    private final EntityManager entityManager;
    private int gametime;

    private CharacterEntity character;

    public Game(Main main) {
        this.main = main;
        this.dataManager = new DataManager();
        this.tickManager = new TickManager(main);
        this.inputManager = new InputManager(main);
        this.commandManager = new CommandManager(main);
        this.entityManager = new EntityManager(main);

        dataManager.load();

        Gdx.input.setInputProcessor(new InputMultiplexer(commandManager, inputManager));
    }

    public void init() {
        character = entityManager.addEntity(new CharacterEntity(new Vector2(32, 32)), new Vector2(100, 100));
        EnemyEntity enemy = entityManager.addEntity(new EnemyEntity(new Vector2(32, 32)), new Vector2(200, 200));
    }

    public void run(float delta) {
        ++gametime;

        commandManager.update(gametime);
        tickManager.tick(gametime);
        entityManager.checkCollisions();
    }

    public Main getMain() {
        return main;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public TickManager getTickManager() {
        return tickManager;
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

    public CharacterEntity getCharacter() {
        return character;
    }

    public void dispose() {
    }
}
