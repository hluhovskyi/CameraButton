package com.dewarder.camerabutton.rx;

import android.support.annotation.NonNull;

import com.dewarder.camerabutton.CameraButton;

public final class HoldStartEvent extends HoldEvent {

    private final CameraButton button;

    private HoldStartEvent(CameraButton button) {
        this.button = button;
    }

    public static HoldStartEvent create(@NonNull CameraButton button) {
        return new HoldStartEvent(button);
    }

    @NonNull
    @Override
    public CameraButton view() {
        return button;
    }
}
