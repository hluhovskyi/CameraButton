package com.dewarder.camerabutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.Px;
import android.support.annotation.StyleableRes;
import android.support.v4.content.ContextCompat;

final class TypedArrayHelper {

    private TypedArrayHelper() {
        throw new InstantiationError();
    }

    @Px
    static int getDimension(Context context,
                            TypedArray array,
                            @StyleableRes int attr,
                            @DimenRes int defaultDimenRes) {

        return array.getDimensionPixelOffset(
                attr, context.getResources().getDimensionPixelSize(defaultDimenRes));
    }

    @ColorInt
    static int getColor(Context context,
                        TypedArray array,
                        @StyleableRes int attr,
                        @ColorRes int defaultColorRes) {

        return array.getColor(
                attr, ContextCompat.getColor(context, defaultColorRes));
    }

    @ColorInt
    static int[] getColors(Context context,
                           TypedArray array,
                           @StyleableRes int attr,
                           @ArrayRes int defaultColorsRes) {

        return context.getResources().getIntArray(
                array.getResourceId(attr, defaultColorsRes));
    }

    static int getInteger(Context context,
                          TypedArray array,
                          @StyleableRes int attr,
                          @IntegerRes int defaultIntRes) {

        return array.getInteger(
                attr, context.getResources().getInteger(defaultIntRes));
    }
}
