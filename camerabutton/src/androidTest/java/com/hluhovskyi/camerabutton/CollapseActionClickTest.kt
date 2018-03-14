/*
 * Copyright (C) 2018 Artem Hluhovskyi
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
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.hluhovskyi.camerabutton.util.pressAndHold
import com.hluhovskyi.camerabutton.util.release
import com.hluhovskyi.camerabutton.util.state
import com.hluhovskyi.camerabutton.util.waitFor
import org.junit.Test

class CollapseActionClickTest : BaseStateTest() {

    override fun setUp() {
        super.setUp()
        activityRule.activity.button.apply {
            collapseAction = CameraButton.Action.CLICK
        }
    }

    @Test
    fun testOnReleaseNotCollapsing() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() + expandDuration()))
                .check(matches(state(CameraButton.State.EXPANDED)))
                .perform(release())
                .perform(waitFor(collapseDuration()))
                .check(matches(state(CameraButton.State.EXPANDED)))
    }

    @Test
    fun testOnTapStartCollapsing() {
        //Longer collapse duration since click need more time
        //for performing an action
        activityRule.activity.button.apply {
            collapseDuration = 1000
        }

        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDuration()))
                .perform(release())
                .perform(waitFor(1000))
                .check(matches(state(CameraButton.State.EXPANDED)))
                .perform(click())
                .check(matches(state(CameraButton.State.START_COLLAPSING)))
    }

    @Test
    fun testOnSecondPressNotCollapsing() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() + expandDuration()))
                .perform(release())
                .perform(waitFor(400)) // Wait any period of time
                .perform(pressAndHold())
                .check(matches(state(CameraButton.State.EXPANDED)))
    }

    @Test
    fun testOnSecondReleaseStartCollapsing() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() + expandDuration()))
                .perform(release())
                .perform(waitFor(400)) // Wait any period of time
                .perform(pressAndHold())
                .perform(release())
                .check(matches(state(CameraButton.State.START_COLLAPSING)))
    }
}