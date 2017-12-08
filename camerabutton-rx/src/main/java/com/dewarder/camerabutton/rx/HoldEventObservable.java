package com.dewarder.camerabutton.rx;

import com.dewarder.camerabutton.CameraButton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class HoldEventObservable extends Observable<HoldEvent> {

    private final CameraButton button;

    HoldEventObservable(CameraButton button) {
        this.button = button;
    }

    @Override
    protected void subscribeActual(Observer<? super HoldEvent> observer) {
        if (!Preconditions.checkMainThread(observer)) {
            return;
        }
        Listener listener = new Listener(button, observer);
        observer.onSubscribe(listener);
        button.setOnHoldEventListener(listener);
    }

    static final class Listener extends MainThreadDisposable implements CameraButton.OnHoldEventListener {

        private final CameraButton button;
        private final Observer<? super HoldEvent> observer;

        Listener(CameraButton button, Observer<? super HoldEvent> observer) {
            this.button = button;
            this.observer = observer;
        }

        @Override
        public void onStart() {
            if (!isDisposed()) {
                observer.onNext(HoldStartEvent.create(button));
            }
        }

        @Override
        public void onFinish() {
            if (!isDisposed()) {
                observer.onNext(HoldFinishEvent.create(button));
            }
        }

        @Override
        public void onCancel() {
            if (!isDisposed()) {
                observer.onNext(HoldCancelEvent.create(button));
            }
        }

        @Override
        protected void onDispose() {
            button.setOnHoldEventListener(null);
        }
    }
}
