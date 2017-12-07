package com.dewarder.camerabutton.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dewarder.camerabutton.CameraButton;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.parameter.selector.FlashSelectors;
import io.fotoapparat.parameter.update.UpdateRequest;
import io.fotoapparat.view.CameraView;

public abstract class BaseActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;

    private CameraView mCameraView;
    private CameraButton mCameraButton;

    private Fotoapparat mCameraManager;
    private boolean mIsFlashEnabled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraView = findViewById(R.id.camera_view);
        mCameraButton = findViewById(R.id.camera_button);

        findViewById(R.id.flash_switch).setOnClickListener(v -> switchFlash());
        findViewById(R.id.camera_switch).setOnClickListener(v -> switchCamera());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        }
    }

    private void openCamera() {
        mCameraManager = Fotoapparat.with(this)
                .into(mCameraView)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCameraManager != null) {
            mCameraManager.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraManager != null) {
            mCameraManager.stop();
        }
    }

    private void switchFlash() {
        mCameraManager.updateParameters(UpdateRequest.builder()
                .flash(mIsFlashEnabled
                        ? FlashSelectors.off()
                        : FlashSelectors.torch())
                .build());
        mIsFlashEnabled = !mIsFlashEnabled;
    }

    private void switchCamera() {
        Toast.makeText(this, "Imagine that camera is switched!", Toast.LENGTH_LONG).show();
    }

    @NonNull
    public CameraButton getCameraButton() {
        return mCameraButton;
    }
}
