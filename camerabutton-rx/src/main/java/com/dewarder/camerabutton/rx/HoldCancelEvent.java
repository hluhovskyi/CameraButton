package com.dewarder.camerabutton.rx;

import android.support.annotation.NonNull;

import com.dewarder.camerabutton.CameraButton;

public final class HoldCancelEvent extends HoldEvent {

    private final CameraButton button;

    private HoldCancelEvent(CameraButton button) {
        this.button = button;
    }

    public static HoldCancelEvent create(@NonNull CameraButton button) {
        return new HoldCancelEvent(button);
    }

    @NonNull
    @Override
    public CameraButton view() {
        return button;
    }
}
