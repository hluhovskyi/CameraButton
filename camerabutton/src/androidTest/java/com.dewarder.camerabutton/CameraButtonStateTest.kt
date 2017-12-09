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

package com.dewarder.camerabutton

import android.os.SystemClock.sleep
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.dewarder.camerabutton.util.disposeHoldActions
import com.dewarder.camerabutton.util.pressAndHold
import com.dewarder.camerabutton.util.state
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CameraButtonStateTest {

    @get:Rule
    val activityRule: ActivityTestRule<CameraButtonActivity>
        = ActivityTestRule(CameraButtonActivity::class.java)

    @After
    fun tearDown() {
        disposeHoldActions()
    }

    @Test
    fun testButtonOnPressHasPressedState() {
        onView(withId(android.R.id.button1))
            .perform(pressAndHold())
            .check(matches(state(CameraButton.State.PRESSED)))
    }

    @Test
    fun testButtonOnHoldAfterDelayHasStartExpandingState() {
        val testExpandDelay = 200L
        val testExpandDuration = 400L

        activityRule.activity.button.apply {
            expandDelay = testExpandDelay
            expandDuration = testExpandDuration
        }

        onView(withId(android.R.id.button1))
            .perform(pressAndHold())
        sleep(testExpandDelay)
        onView(withId(android.R.id.button1))
            .check(matches(state(CameraButton.State.START_EXPANDING)))
    }
}
