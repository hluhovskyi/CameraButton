package com.dewarder.camerabutton;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

final class Interpolators {

    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static final float INTERPOLATING_ARC_WIDTH_FACTOR_FROM = 0.4f;

    static Interpolator getLinearInterpolator() {
        return LINEAR_INTERPOLATOR;
    }

    static float interpolateArcWidth(float factor) {
        if (factor < INTERPOLATING_ARC_WIDTH_FACTOR_FROM) {
            return 0;
        } else {
            return (factor - INTERPOLATING_ARC_WIDTH_FACTOR_FROM) / (1 - INTERPOLATING_ARC_WIDTH_FACTOR_FROM);
        }
    }
}
