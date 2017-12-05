package com.dewarder.camerabutton.rx;

import com.dewarder.camerabutton.CameraButton;

import io.reactivex.Observable;

public final class RxCameraButton {

    private RxCameraButton() {
        throw new InstantiationError();
    }

    public static Observable<CameraButton.State> stateChanges(CameraButton button) {
        return new StateChangeObservable(button);
    }
}
