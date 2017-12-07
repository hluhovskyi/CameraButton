package com.dewarder.camerabutton.rx;

import com.dewarder.camerabutton.CameraButton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class ProgressObservable extends Observable<Float> {

    private final CameraButton button;

    ProgressObservable(CameraButton button) {
        this.button = button;
    }

    @Override
    protected void subscribeActual(Observer<? super Float> observer) {
        if (!Predictions.checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener(button, observer);
        observer.onSubscribe(listener);
        button.setOnProgressChangeListener(listener);
    }

    static final class Listener extends MainThreadDisposable implements CameraButton.OnProgressChangeListener {

        private final CameraButton button;
        private final Observer<? super Float> observer;

        Listener(CameraButton button, Observer<? super Float> observer) {
            this.button = button;
            this.observer = observer;
        }

        @Override
        public void onProgressChanged(float progress) {
            if (!isDisposed()) {
                observer.onNext(progress);
            }
        }

        @Override
        protected void onDispose() {
            button.setOnProgressChangeListener(null);
        }
    }
}
