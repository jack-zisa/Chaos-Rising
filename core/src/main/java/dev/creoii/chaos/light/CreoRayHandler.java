package dev.creoii.chaos.light;

import box2dLight.RayHandler;

public class CreoRayHandler extends RayHandler {
    public CreoRayHandler(int fboWidth, int fboHeight) {
        super(null, fboWidth, fboHeight);
    }
    public CreoRayHandler() {
        super(null);
    }
}
