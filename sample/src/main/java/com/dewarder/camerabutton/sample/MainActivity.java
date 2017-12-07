package com.dewarder.camerabutton.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.dewarder.camerabutton.CameraButton;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getCameraButton().setOnTapEventListener(this::makePhoto);
        getCameraButton().setOnHoldEventListener(new CameraButton.OnHoldEventListener() {
            @Override
            public void onStart() {
                startRecordVideo();
            }

            @Override
            public void onFinish() {
                finishRecordVideo();
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void makePhoto() {
        Toast.makeText(this, "Photo has been made!", Toast.LENGTH_SHORT).show();
    }

    private void startRecordVideo() {
        Toast.makeText(this, "Start recording video...", Toast.LENGTH_SHORT).show();
    }

    private void finishRecordVideo() {
        Toast.makeText(this, "Finish recording video...", Toast.LENGTH_SHORT).show();
    }
}
