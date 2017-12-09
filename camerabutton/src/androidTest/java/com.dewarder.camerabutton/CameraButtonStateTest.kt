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
import com.dewarder.camerabutton.CameraButton.State
import com.dewarder.camerabutton.util.disposeHoldActions
import com.dewarder.camerabutton.util.pressAndHold
import com.dewarder.camerabutton.util.release
import com.dewarder.camerabutton.util.state
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CameraButtonStateTest {

    @get:Rule
    val activityRule: ActivityTestRule<CameraButtonActivity>
        = ActivityTestRule(CameraButtonActivity::class.java)

    @Before
    fun setUp() {
        activityRule.activity.button.apply {
            expandDelay = EXPAND_DELAY
            expandDuration = EXPAND_DURATION
            collapseDuration = COLLAPSE_DURATION
        }
    }

    @After
    fun tearDown() {
        disposeHoldActions()
    }

    @Test
    fun testButtonOnPressHasPressedState() {
        onView(withId(android.R.id.button1))
            .perform(pressAndHold())
            .check(matches(state(State.PRESSED)))
    }

    @Test
    fun testButtonOnHoldAfterDelayHasStartExpandingState() {
        onView(withId(android.R.id.button1))
            .perform(pressAndHold())
        sleep(EXPAND_DELAY)
        onView(withId(android.R.id.button1))
            .check(matches(state(State.START_EXPANDING)))
    }

    @Test
    fun testButtonOnHoldAfterDelayHasExpandedState() {
        onView(withId(android.R.id.button1))
            .perform(pressAndHold())
        sleep(EXPAND_DELAY + EXPAND_DURATION)
        onView(withId(android.R.id.button1))
            .check(matches(state(State.EXPANDED)))
    }

    @Test
    fun testButtonReleaseWithoutDelayHasDefaultState() {
        onView(withId(android.R.id.button1))
            .perform(pressAndHold())
            .perform(release())
            .check(matches(state(State.DEFAULT)))
    }

    @Test
    fun testButtonReleaseAfterDelayHasStartCollapsingState() {
        onView(withId(android.R.id.button1))
            .perform(pressAndHold())
        sleep(EXPAND_DELAY + EXPAND_DURATION)
        onView(withId(android.R.id.button1))
            .check(matches(state(State.EXPANDED)))
            .perform(release())
            .check(matches(state(State.START_COLLAPSING)))
    }

    @Test
    fun testButtonReleaseAfterDelayHasDefaultState() {
        onView(withId(android.R.id.button1))
            .perform(pressAndHold())
        sleep(EXPAND_DELAY + EXPAND_DURATION)
        onView(withId(android.R.id.button1))
            .perform(release())
        sleep(COLLAPSE_DURATION)
        onView(withId(android.R.id.button1))
            .check(matches(state(State.DEFAULT)))

    }

    private companion object {
        private const val EXPAND_DELAY = 200L
        private const val EXPAND_DURATION = 400L
        private const val COLLAPSE_DURATION = 400L
    }
}
