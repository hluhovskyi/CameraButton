package com.dewarder.camerabutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DimenRes;
import android.support.annotation.Px;
import android.support.annotation.StyleableRes;

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
}
