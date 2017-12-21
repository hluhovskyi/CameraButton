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

package com.dewarder.camerabutton.rxjava2;

import android.support.annotation.NonNull;

import com.dewarder.camerabutton.CameraButton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class StateChangeObservable extends Observable<CameraButton.State> {

    private final CameraButton button;

    StateChangeObservable(CameraButton button) {
        this.button = button;
    }

    @Override
    protected void subscribeActual(Observer<? super CameraButton.State> observer) {
        if (!Preconditions.checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener(button, observer);
        observer.onSubscribe(listener);
        button.setOnStateChangeListener(listener);
    }

    static final class Listener extends MainThreadDisposable implements CameraButton.OnStateChangeListener {

        private final CameraButton button;
        private final Observer<? super CameraButton.State> observer;

        Listener(CameraButton button, Observer<? super CameraButton.State> observer) {
            this.button = button;
            this.observer = observer;
        }

        @Override
        public void onStateChanged(@NonNull CameraButton.State state) {
            if (!isDisposed()) {
                observer.onNext(state);
            }
        }

        @Override
        protected void onDispose() {
            button.setOnStateChangeListener(null);
        }
    }
}
