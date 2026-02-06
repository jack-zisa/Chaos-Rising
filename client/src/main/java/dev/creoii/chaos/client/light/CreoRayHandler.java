package dev.creoii.chaos.client.light;

import box2dLight.RayHandler;

public class CreoRayHandler extends RayHandler {
    public CreoRayHandler(int fboWidth, int fboHeight) {
        super(null, fboWidth, fboHeight);
        setupDefault();
    }

    public CreoRayHandler() {
        super(null);
        setupDefault();
    }

    private void setupDefault() {
        setBlurNum(3);
        setShadows(true);
        setBlur(true);
        setGammaCorrection(true);
        //useDiffuseLight(true);
    }
}
