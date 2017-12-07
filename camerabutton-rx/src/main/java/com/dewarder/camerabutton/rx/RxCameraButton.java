package com.dewarder.camerabutton.rx;

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
