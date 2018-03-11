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

package com.hluhovskyi.camerabutton.rxjava2;

import android.support.annotation.NonNull;

import com.hluhovskyi.camerabutton.CameraButton;

public final class StateChangeEvent {

    private final CameraButton button;
    private final CameraButton.State state;

    private StateChangeEvent(CameraButton button, CameraButton.State state) {
        this.button = button;
        this.state = state;
    }

    public static StateChangeEvent create(@NonNull CameraButton button, CameraButton.State state) {
        return new StateChangeEvent(button, state);
    }

    @NonNull
    public CameraButton view() {
        return button;
    }

    public final CameraButton component1() {
        return view();
    }

    public final CameraButton.State component2() {
        return state;
    }

}
