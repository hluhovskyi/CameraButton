/*
 * Copyright (C) 2017 Artem Hluhovskyi
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

package com.hluhovskyi.camerabutton;

public final class Constraints {

    private Constraints() {
        throw new InstantiationError();
    }

    public static <T> T checkNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException("Non-null object required");
        }
        return obj;
    }

    static int checkDimension(int dimension) {
        if (dimension <= 0) {
            throw new IllegalStateException("Dimension should be greater than 0");
        }
        return dimension;
    }

    static long checkDuration(long duration) {
        if (duration <= 0) {
            throw new IllegalStateException("Duration should be greater than 0");
        }
        return duration;
    }
}
