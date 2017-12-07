package com.dewarder.camerabutton.rx;

import android.support.annotation.NonNull;

import com.dewarder.camerabutton.CameraButton;

public final class HoldFinishEvent extends HoldEvent {

    private final CameraButton button;

    private HoldFinishEvent(CameraButton button) {
        this.button = button;
    }

    public static HoldFinishEvent create(@NonNull CameraButton button) {
        return new HoldFinishEvent(button);
    }

    @NonNull
    @Override
    public CameraButton view() {
        return button;
    }
}
