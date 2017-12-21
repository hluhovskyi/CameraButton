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

package com.dewarder.camerabutton.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.dewarder.camerabutton.CameraButton;

public class MainActivity extends BaseActivity {

    private static final long ANIMATION_TRANSLATION_DURATION = 200L;

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

        getCameraButton().setOnStateChangeListener(this::onStateChanged);
    }

    private void onStateChanged(CameraButton.State state) {
        if (state == CameraButton.State.START_EXPANDING) {
            translateToRight(getFlashSwitch(), false);
            translateToLeft(getCameraSwitch(), false);
        } else if (state == CameraButton.State.START_COLLAPSING) {
            translateToRight(getFlashSwitch(), true);
            translateToLeft(getCameraSwitch(), true);
        }
    }

    void makePhoto() {
    }

    void startRecordVideo() {
    }

    void finishRecordVideo() {
    }

    private static void translateToRight(View view, boolean show) {
        float x = show ? 0f : view.getWidth();
        float alpha = show ? 1f : 0f;
        view.animate().translationX(-x)
                .alpha(alpha)
                .setDuration(ANIMATION_TRANSLATION_DURATION);
    }

    private static void translateToLeft(View view, boolean show) {
        float x = show ? 0f : view.getWidth();
        float alpha = show ? 1f : 0f;
        view.animate().translationX(x)
                .alpha(alpha)
                .setDuration(ANIMATION_TRANSLATION_DURATION);
    }
}
