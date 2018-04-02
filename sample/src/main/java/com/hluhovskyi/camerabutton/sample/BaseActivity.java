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

package com.hluhovskyi.camerabutton.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.hluhovskyi.camerabutton.CameraButton;

public abstract class BaseActivity extends AppCompatActivity {

    private View mFlashSwitch;
    private View mCameraSwitch;
    private RecyclerView mModesRecycler;
    private CameraButton mCameraButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraButton = findViewById(R.id.camera_button);

        mFlashSwitch = findViewById(R.id.flash_switch);
        mFlashSwitch.setOnClickListener(v -> switchFlash());
        mModesRecycler = findViewById(R.id.recycler);

        mCameraSwitch = findViewById(R.id.camera_switch);
        mCameraSwitch.setOnClickListener(v -> switchCamera());
    }

    private void switchFlash() {
        Toast.makeText(this, "Imagine that flash is switched!", Toast.LENGTH_LONG).show();
    }

    private void switchCamera() {
        Toast.makeText(this, "Imagine that camera is switched!", Toast.LENGTH_LONG).show();
    }

    public RecyclerView getModesRecycler() {
        return mModesRecycler;
    }

    @NonNull
    public View getFlashSwitch() {
        return mFlashSwitch;
    }

    @NonNull
    public View getCameraSwitch() {
        return mCameraSwitch;
    }

    public CameraButton getCameraButton() {
        return mCameraButton;
    }
}
