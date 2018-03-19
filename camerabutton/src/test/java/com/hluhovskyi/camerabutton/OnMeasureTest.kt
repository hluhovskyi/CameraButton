package com.hluhovskyi.camerabutton

import android.support.annotation.DimenRes
import android.view.View
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class OnMeasureTest {

    private lateinit var button: CameraButton

    @Before
    fun setUp() {
        button = CameraButton(RuntimeEnvironment.application)
    }

    @Test
    fun atMost() {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST)

        button.onMeasure(widthSpec, heightSpec)

        assertEquals(getDimen(R.dimen.cb_layout_width_default), button.measuredWidth)
        assertEquals(getDimen(R.dimen.cb_layout_height_default), button.measuredHeight)
    }

    @Test
    fun unspecified() {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        button.onMeasure(widthSpec, heightSpec)

        assertEquals(getDimen(R.dimen.cb_layout_width_default), button.measuredWidth)
        assertEquals(getDimen(R.dimen.cb_layout_height_default), button.measuredHeight)
    }

    @Test
    fun exactly() {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY)

        button.onMeasure(widthSpec, heightSpec)

        assertEquals(100, button.measuredWidth)
        assertEquals(100, button.measuredHeight)
    }

    private fun getDimen(@DimenRes dimenRes: Int) =
            RuntimeEnvironment.application.resources
                    .getDimensionPixelSize(R.dimen.cb_layout_width_default)
}