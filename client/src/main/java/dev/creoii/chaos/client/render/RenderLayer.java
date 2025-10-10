package dev.creoii.chaos.client.render;

public enum RenderLayer {
    GROUND(RenderSpace.WORLD),
    ENTITY(RenderSpace.WORLD),
    OBJECT(RenderSpace.WORLD),
    HUD(RenderSpace.SCREEN),
    GUI(RenderSpace.SCREEN);

    private final RenderSpace space;

    RenderLayer(RenderSpace space) {
        this.space = space;
    }

    public RenderSpace getSpace() {
        return space;
    }
}
