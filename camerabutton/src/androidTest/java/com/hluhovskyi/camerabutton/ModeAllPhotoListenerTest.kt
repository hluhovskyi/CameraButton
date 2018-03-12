/*
 * Copyright (C) 2018 Artem Glugovsky
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
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.hluhovskyi.camerabutton.util.pressAndHold
import com.hluhovskyi.camerabutton.util.release
import com.hluhovskyi.camerabutton.util.waitFor
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class ModeAllPhotoListenerTest : BaseStateTest() {

    @Mock
    private lateinit var listener: CameraButton.OnPhotoEventListener

    override fun setUp() {
        super.setUp()

        activityRule.activity.button.setOnPhotoEventListener(listener)
    }

    @Test
    fun onClick() {
        onView(withId(buttonId()))
                .perform(click())

        verify(listener).onClick()
    }

    @Test
    fun onShortHoldAndRelease() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() / 2))
                .perform(release())

        verify(listener).onClick()
    }

    @Test
    fun onHoldAndReleaseStartExpanding() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay()))
                .perform(release())

        verify(listener).onClick()
    }

    @Test
    fun onHoldAndReleaseExpanded() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() + expandDuration()))
                .perform(release())

        verify(listener, never()).onClick()
    }

}