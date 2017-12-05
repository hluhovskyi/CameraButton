package com.dewarder.camerabutton.rx;

import android.support.annotation.NonNull;

import com.dewarder.camerabutton.CameraButton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class StateChangeObservable extends Observable<CameraButton.State> {

    private final CameraButton button;

    public StateChangeObservable(CameraButton button) {
        this.button = button;
    }

    @Override
    protected void subscribeActual(Observer<? super CameraButton.State> observer) {
        //TODO: add main thread check
        Listener listener = new Listener(button, observer);
        observer.onSubscribe(listener);
        button.setOnStateChangeListener(listener);
    }

    static final class Listener extends MainThreadDisposable implements CameraButton.OnStateChangeListener {

        private final CameraButton button;
        private final Observer<? super CameraButton.State> observer;

        public Listener(CameraButton button, Observer<? super CameraButton.State> observer) {
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
