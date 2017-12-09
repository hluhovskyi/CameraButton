/*
 * Copyright (C) 2017 Artem Glugovsky
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
