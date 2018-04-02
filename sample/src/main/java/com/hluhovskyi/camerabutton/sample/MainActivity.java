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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hluhovskyi.camerabutton.CameraButton;
import com.hluhovskyi.camerabutton.recyclerview.CameraButtonRecyclerView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final long ANIMATION_TRANSLATION_DURATION = 200L;

    static final List<String> CAMERA_MODE_NAMES = Collections.unmodifiableList(Arrays.asList(
            "Beach",
            "Business center",
            "Casino",
            "Fitness center",
            "Spa"
    ));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCameraButton().setIcons(new Bitmap[]{
                BitmapHelper.getBitmap(this, R.drawable.ic_beach_access_28dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_business_center_28dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_casino_28dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_fitness_center_24dp),
                BitmapHelper.getBitmap(this, R.drawable.ic_spa_28dp),
        });

        getCameraButton().setOnPhotoEventListener(new CameraButton.OnPhotoEventListener() {
            @Override
            public void onClick() {
                makePhoto();
            }
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

        getModesRecycler().setAdapter(new CameraModeAdapter());
        getModesRecycler().setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        /*
         * ATTENTION!
         *
         * Usage of method below can trigger blow up process of your phone.
         * Use it with strong feeling of that risk.
         */
        CameraButtonRecyclerView.newBuilder(getCameraButton(), getModesRecycler())
                .snap()
                .attach();
    }

    private void onStateChanged(CameraButton.State state) {
        if (state == CameraButton.State.START_EXPANDING) {
            translateToRight(getFlashSwitch(), false);
            translateToLeft(getCameraSwitch(), false);
            translateToBottom(getModesRecycler(), false);
        } else if (state == CameraButton.State.START_COLLAPSING) {
            translateToRight(getFlashSwitch(), true);
            translateToLeft(getCameraSwitch(), true);
            translateToBottom(getModesRecycler(), true);
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

    private static void translateToBottom(View view, boolean show) {
        float y = show ? 0f : view.getHeight();
        float alpha = show ? 1f : 0f;
        view.animate().translationY(y)
                .alpha(alpha)
                .setDuration(ANIMATION_TRANSLATION_DURATION);
    }

    private static class CameraModeAdapter extends RecyclerView.Adapter<CameraModeAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_camera_mode, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mName.setText(CAMERA_MODE_NAMES.get(position));
        }

        @Override
        public int getItemCount() {
            return CAMERA_MODE_NAMES.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mName;

            ViewHolder(View itemView) {
                super(itemView);
                mName = itemView.findViewById(R.id.name);
            }
        }
    }
}
