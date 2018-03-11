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

package com.hluhovskyi.camerabutton.rxjava2;

import com.hluhovskyi.camerabutton.CameraButton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class TapEventObservable extends Observable<TapEvent> {

    private final CameraButton button;

    TapEventObservable(CameraButton button) {
        this.button = button;
    }

    @Override
    protected void subscribeActual(Observer<? super TapEvent> observer) {
        if (!Preconditions.checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener(button, observer);
        observer.onSubscribe(listener);
        button.setOnTapEventListener(listener);
    }

    static final class Listener extends MainThreadDisposable implements CameraButton.OnTapEventListener {

        private final CameraButton button;
        private final Observer<? super TapEvent> observer;

        Listener(CameraButton button, Observer<? super TapEvent> observer) {
            this.button = button;
            this.observer = observer;
        }

        @Override
        public void onTap() {
            if (!isDisposed()) {
                observer.onNext(TapEvent.create(button));
            }
        }

        @Override
        protected void onDispose() {
            button.setOnTapEventListener(null);
        }
    }
}
