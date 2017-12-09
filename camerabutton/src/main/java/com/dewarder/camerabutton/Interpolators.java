/*
 * Copyright (C) 2017 Artem Glugovsky
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
