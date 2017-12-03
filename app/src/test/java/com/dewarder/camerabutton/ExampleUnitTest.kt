package com.dewarder.camerabutton

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        assertNotNull(Shadows.shadowOf(RuntimeEnvironment.application))
        val attrs = Robolectric.buildAttributeSet()
            .addAttribute(R.dimen.cb_main_circle_radius_default, "cb_main_circle_radius")
            .build()

        val button = CameraButton(RuntimeEnvironment.application, attrs)
        assertEquals(button.mMainCircleRadius, 20)
    }
}
