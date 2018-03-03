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

package com.dewarder.camerabutton

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.dewarder.camerabutton.CameraButton.State
import com.dewarder.camerabutton.util.pressAndHold
import com.dewarder.camerabutton.util.release
import com.dewarder.camerabutton.util.state
import com.dewarder.camerabutton.util.waitFor
import org.junit.Test

class ModeAllStateTest : BaseStateTest() {

    @Test
    fun testOnPressHasPressedState() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .check(matches(state(State.PRESSED)))
    }

    @Test
    fun testOnHoldAfterDelayHasStartExpandingState() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay()))
                .check(matches(state(State.START_EXPANDING)))
    }

    @Test
    fun testOnHoldAfterDelayHasExpandedState() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() + expandDuration()))
                .check(matches(state(State.EXPANDED)))
    }

    @Test
    fun testOnReleaseWithoutDelayHasDefaultState() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(release())
                .check(matches(state(State.DEFAULT)))
    }

    @Test
    fun testOnReleaseAfterDelayHasStartCollapsingState() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() + expandDuration()))
                .check(matches(state(State.EXPANDED)))
                .perform(release())
                .check(matches(state(State.START_COLLAPSING)))
    }

    @Test
    fun testOnReleaseAfterDelayHasDefaultState() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() + expandDuration()))
                .perform(release())
                .perform(waitFor(collapseDuration()))
                .check(matches(state(State.DEFAULT)))
    }
}
