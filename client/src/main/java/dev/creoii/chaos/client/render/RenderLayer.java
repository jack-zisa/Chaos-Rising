package dev.creoii.chaos.client.render;

public enum RenderLayer {
    GROUND(RenderSpace.WORLD),
    ENTITY(RenderSpace.WORLD),
    OBJECT(RenderSpace.WORLD),
    HUD(RenderSpace.SCREEN),
    GUI(RenderSpace.SCREEN, false);

    private final RenderSpace space;
    private final boolean blending;

    RenderLayer(RenderSpace space, boolean blending) {
        this.space = space;
        this.blending = blending;
    }

    RenderLayer(RenderSpace space) {
        this(space, true);
    }

    public RenderSpace getSpace() {
        return space;
    }

    public boolean isBlending() {
        return blending;
    }
}
