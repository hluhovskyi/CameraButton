package com.dewarder.camerabutton.rx;

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
}
