/*
 * Copyright (C) 2018 Artem Hluhovskyi
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

package com.hluhovskyi.camerabutton.rxjava2;

import com.hluhovskyi.camerabutton.CameraButton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class VideoEventObservable extends Observable<VideoEvent> {

    private final CameraButton button;

    VideoEventObservable(CameraButton button) {
        this.button = button;
    }

    @Override
    protected void subscribeActual(Observer<? super VideoEvent> observer) {
        if (!Preconditions.checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener(button, observer);
        observer.onSubscribe(listener);
        button.setOnVideoEventListener(listener);
    }

    static final class Listener extends MainThreadDisposable implements CameraButton.OnVideoEventListener {

        private final CameraButton button;
        private final Observer<? super VideoEvent> observer;

        Listener(CameraButton button, Observer<? super VideoEvent> observer) {
            this.button = button;
            this.observer = observer;
        }

        @Override
        public void onStart() {
            if (!isDisposed()) {
                observer.onNext(VideoStartEvent.create(button));
            }
        }

        @Override
        public void onFinish() {
            if (!isDisposed()) {
                observer.onNext(VideoFinishEvent.create(button));
            }
        }

        @Override
        public void onCancel() {
            if (!isDisposed()) {
                observer.onNext(VideoCancelEvent.create(button));
            }
        }

        @Override
        protected void onDispose() {
            button.setOnVideoEventListener(null);
        }
    }
}
