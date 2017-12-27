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

package com.dewarder.camerabutton.rxjava2;

import android.support.annotation.NonNull;

import com.dewarder.camerabutton.CameraButton;

public final class TapEvent {

    private final CameraButton button;

    private TapEvent(CameraButton button) {
        this.button = button;
    }

    public static TapEvent create(@NonNull CameraButton button) {
        return new TapEvent(button);
    }

    @NonNull
    public CameraButton view() {
        return button;
    }

    public final CameraButton component1() {
        return view();
    }
}
