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

package com.dewarder.camerabutton.rxjava2;

import android.support.annotation.NonNull;

import com.dewarder.camerabutton.CameraButton;
import com.dewarder.camerabutton.Constraints;

import io.reactivex.Observable;

public final class RxCameraButton {

    private RxCameraButton() {
        throw new InstantiationError();
    }

    public static Observable<CameraButton.State> stateChanges(@NonNull CameraButton button) {
        Constraints.checkNonNull(button);
        return new StateChangeObservable(button);
    }

    public static Observable<Float> progress(@NonNull CameraButton button) {
        Constraints.checkNonNull(button);
        return new ProgressObservable(button);
    }

    public static Observable<TapEvent> tapEvents(@NonNull CameraButton button) {
        Constraints.checkNonNull(button);
        return new TapEventObservable(button);
    }

    public static Observable<HoldEvent> holdEvents(@NonNull CameraButton button) {
        Constraints.checkNonNull(button);
        return new HoldEventObservable(button);
    }
}
