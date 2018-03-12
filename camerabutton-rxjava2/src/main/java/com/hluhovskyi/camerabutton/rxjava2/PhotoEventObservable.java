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

final class PhotoEventObservable extends Observable<PhotoEvent> {

    private final CameraButton button;

    PhotoEventObservable(CameraButton button) {
        this.button = button;
    }

    @Override
    protected void subscribeActual(Observer<? super PhotoEvent> observer) {
        if (!Preconditions.checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener(button, observer);
        observer.onSubscribe(listener);
        button.setOnPhotoEventListener(listener);
    }

    static final class Listener extends MainThreadDisposable implements CameraButton.OnPhotoEventListener {

        private final CameraButton button;
        private final Observer<? super PhotoEvent> observer;

        Listener(CameraButton button, Observer<? super PhotoEvent> observer) {
            this.button = button;
            this.observer = observer;
        }

        @Override
        public void onClick() {
            if (!isDisposed()) {
                observer.onNext(PhotoEvent.create(button));
            }
        }

        @Override
        protected void onDispose() {
            button.setOnPhotoEventListener(null);
        }
    }
}
