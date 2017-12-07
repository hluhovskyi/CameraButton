package com.dewarder.camerabutton.rx;

import android.os.Looper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposables;

final class Predictions {

    private Predictions() {
        throw new InstantiationError();
    }

    static boolean checkMainThread(Observer<?> observer) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            observer.onSubscribe(Disposables.empty());
            observer.onError(new IllegalStateException(
                    "Expected to be called on the main thread but was " + Thread.currentThread().getName()));
            return false;
        }
        return true;
    }
}
