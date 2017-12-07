package com.dewarder.camerabutton;

public final class Constraints {

    private Constraints() {
        throw new InstantiationError();
    }

    public static <T> T checkNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException("Non-null object required");
        }
        return obj;
    }

    static int checkDimension(int dimension) {
        if (dimension <= 0) {
            throw new IllegalStateException("Dimension should be greater than 0");
        }
        return dimension;
    }

    static long checkDuration(long duration) {
        if (duration <= 0) {
            throw new IllegalStateException("Duration should be greater than 0");
        }
        return duration;
    }
}
