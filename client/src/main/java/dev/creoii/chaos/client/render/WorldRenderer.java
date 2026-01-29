package dev.creoii.chaos.client.render;

import com.badlogic.gdx.utils.Disposable;
import dev.creoii.chaos.client.ClientWorld;

public class WorldRenderer implements Disposable {
    private final ClientWorld world;
    private final StatusTextManager statusTextManager;

    public WorldRenderer(ClientWorld world) {
        this.world = world;
        world.getGame().getRenderer().registerRenderable(RenderLayer.ENTITY, world.getEntityManager());
        world.getGame().getRenderer().registerRenderable(RenderLayer.ENTITY, statusTextManager = new StatusTextManager());
    }

    public ClientWorld getWorld() {
        return world;
    }

    public void render(float delta, Renderer renderer, boolean debug) {
        renderer.getBatch().disableBlending();
        world.getMapRenderer().setView(renderer.getCamera());
        world.getMapRenderer().render();
        renderer.getBatch().enableBlending();
    }

    public StatusTextManager getStatusTextManager() {
        return statusTextManager;
    }

    @Override
    public void dispose() {
        statusTextManager.dispose();
    }
}
