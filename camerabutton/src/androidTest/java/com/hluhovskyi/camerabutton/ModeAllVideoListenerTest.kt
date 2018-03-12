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
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions

class ModeAllVideoListenerTest : BaseStateTest() {

    @Mock
    private lateinit var listener: CameraButton.OnVideoEventListener

    override fun setUp() {
        super.setUp()

        activityRule.activity.button.setOnVideoEventListener(listener)
    }

    @Test
    fun onClick() {
        onView(withId(buttonId()))
                .perform(click())

        verifyZeroInteractions(listener)
    }

    @Test
    fun onShortHold() {
        onView(withId(buttonId()))
                .perform(click())

        verifyZeroInteractions(listener)
    }

    @Test
    fun onHoldAndReleaseWhenStartExpanding() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay()))
                .perform(release())

        verifyZeroInteractions(listener)
    }

    @Test
    fun onHoldAndReleaseWhenExpanded() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() + expandDuration()))
                .perform(release())

        verify(listener).onStart()
        verify(listener).onFinish()
    }

    @Test
    fun onHoldUntilFinished() {
        activityRule.activity.button.videoDuration = videoDuration()

        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() + expandDuration() + videoDuration()))

        verify(listener).onStart()
        verify(listener).onFinish()
    }
}