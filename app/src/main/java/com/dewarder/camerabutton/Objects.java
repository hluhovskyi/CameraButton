package com.dewarder.camerabutton;

final class Objects {

    private Objects() {
        throw new InstantiationError();
    }

    public static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException("Non-null object required");
        }
        return obj;
    }
}
