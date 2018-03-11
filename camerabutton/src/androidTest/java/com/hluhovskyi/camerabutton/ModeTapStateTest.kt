/*
 * Copyright (C) 2017 Artem Hluhovskyi
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

package com.hluhovskyi.camerabutton

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.hluhovskyi.camerabutton.CameraButton.Mode
import com.hluhovskyi.camerabutton.CameraButton.State
import com.hluhovskyi.camerabutton.util.pressAndHold
import com.hluhovskyi.camerabutton.util.release
import com.hluhovskyi.camerabutton.util.state
import com.hluhovskyi.camerabutton.util.waitFor
import org.junit.Test

class ModeTapStateTest : BaseStateTest() {

    override fun setUp() {
        super.setUp()
        activityRule.activity.button.apply {
            mode = Mode.TAP
        }
    }

    @Test
    fun testOnPressHasPressedState() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .check(matches(state(State.PRESSED)))
    }

    @Test
    fun testOnHoldAfterDelayNotExpandedAndHasPressedState() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay()))
                .check(matches(state(State.PRESSED)))
                .perform(waitFor(expandDuration()))
                .check(matches(state(State.PRESSED)))
    }

    @Test
    fun testOnReleaseAfterDelayNotCollapsingAndHasDefaultState() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() + expandDuration()))
                .check(matches(state(State.PRESSED)))
                .perform(release())
                .check(matches(state(State.DEFAULT)))
    }
}