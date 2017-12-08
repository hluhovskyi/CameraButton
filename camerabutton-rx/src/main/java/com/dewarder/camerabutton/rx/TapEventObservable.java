package com.dewarder.camerabutton.rx;

import com.dewarder.camerabutton.CameraButton;

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
