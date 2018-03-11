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

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hluhovskyi.camerabutton.CameraButton;

public class MainActivity extends BaseActivity {

    private static final long ANIMATION_TRANSLATION_DURATION = 200L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCameraButton().setIcons(new Bitmap[]{
                BitmapHelper.getBitmap(this, R.drawable.ic_brightness_1_red_28dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_flash_on_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_brightness_1_red_28dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_flash_on_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_sync_red_36dp),
        });

        getCameraButton().setOnPhotoEventListener(() -> {
            getCameraButton().setIconsPosition(1.5f);
        });
        getCameraButton().setOnVideoEventListener(new CameraButton.OnVideoEventListener() {
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

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                TextView tv = new TextView(parent.getContext());
                tv.setText("Item");
                return new RecyclerView.ViewHolder(tv) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            }

            @Override
            public int getItemCount() {
                return 20;
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                float range = recyclerView.computeHorizontalScrollRange();
                float offset = recyclerView.computeHorizontalScrollOffset();
                getCameraButton().setIconsPosition(20 * offset / range);
            }
        });
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
