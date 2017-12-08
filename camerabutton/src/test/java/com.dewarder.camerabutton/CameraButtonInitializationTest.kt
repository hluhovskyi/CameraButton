package com.dewarder.camerabutton

import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.DimenRes
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class CameraButtonInitializationTest {

    @Test
    fun testMainCircleRadiusDefaultInitializedCorrect() {
        val button = CameraButton(context)
        assertDimenEquals(button.mainCircleRadius, R.dimen.cb_main_circle_radius_default)
    }

    @Test
    fun testMainCircleRadiusCustomInitializedCorrect() {
        val dp = 1
        val attrs = createAttrs(R.attr.cb_main_circle_radius, "${dp}dp")
        val button = CameraButton(context, attrs)
        assertEquals(button.mainCircleRadius, dp)
    }

    @Test
    fun testMainCircleRadiusExpandedDefaultInitializedCorrect() {
        val button = CameraButton(context)
        assertDimenEquals(button.mainCircleRadiusExpanded, R.dimen.cb_main_circle_radius_expanded_default)
    }

    @Test
    fun testMainCircleRadiusExpandedCustomInitializedCorrect() {
        val dp = 1
        val attrs = createAttrs(R.attr.cb_main_circle_radius_expanded, "${dp}dp")
        val button = CameraButton(context, attrs)
        assertEquals(button.mainCircleRadiusExpanded, dp)
    }

    private companion object {

        val context: Context
            get() = RuntimeEnvironment.application

        fun createAttrs(@AttrRes attr: Int, value: String) =
                Robolectric.buildAttributeSet()
                        .addAttribute(attr, value)
                        .build()

        fun assertDimenEquals(actual: Int, @DimenRes expectedRes: Int) {
            assertEquals(actual, context.resources.getDimensionPixelSize(expectedRes))
        }
    }
}