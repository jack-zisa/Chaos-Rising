package dev.creoii.chaos.client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Disposable;
import dev.creoii.chaos.client.ClientWorld;
import dev.creoii.chaos.entity.Entity;

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

    public void renderLight(float delta, Renderer renderer, boolean debug) {
        world.getRayHandler().setCombinedMatrix(renderer.getCamera().combined.cpy().scl(1f / Entity.COORDINATE_SCALE), renderer.getViewport().getScreenX(), renderer.getViewport().getScreenY(), renderer.getViewport().getScreenWidth(), renderer.getViewport().getScreenHeight());
        world.getRayHandler().updateAndRender();
    }

    public StatusTextManager getStatusTextManager() {
        return statusTextManager;
    }

    @Override
    public void dispose() {
        statusTextManager.dispose();
    }
}
