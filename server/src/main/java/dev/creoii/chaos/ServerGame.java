package dev.creoii.chaos;

public class ServerGame implements Game {
    private final ServerMain main;
    private final DataManager dataManager;
    private final OptionsManager optionsManager;
    private final TickManager tickManager;
    private final CollisionManager collisionManager;
    private final EntityManager entityManager;
    private int gametime;

    public ServerGame(ServerMain main) {
        this.main = main;
        dataManager = new DataManager();
        optionsManager = new OptionsManager();
        tickManager = new TickManager(main);
        collisionManager = new CollisionManager(main);
        entityManager = new EntityManager(main);
    }

    public void run(float delta) {
        ++gametime;

        tickManager.tick(gametime, delta);
        collisionManager.checkCollisions();
    }

    public ServerMain getMain() {
        return main;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public OptionsManager getOptionsManager() {
        return optionsManager;
    }

    public TickManager getTickManager() {
        return tickManager;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public int getGametime() {
        return gametime;
    }
}
