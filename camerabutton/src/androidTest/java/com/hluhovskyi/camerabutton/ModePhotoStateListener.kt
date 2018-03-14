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
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.hluhovskyi.camerabutton.util.pressAndHold
import com.hluhovskyi.camerabutton.util.release
import com.hluhovskyi.camerabutton.util.waitFor
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class ModePhotoStateListener : BaseStateTest() {

    @Mock
    private lateinit var listener: CameraButton.OnStateChangeListener
    @Captor
    private lateinit var stateCaptor: ArgumentCaptor<CameraButton.State>

    override fun setUp() {
        super.setUp()

        activityRule.activity.button.apply {
            mode = CameraButton.Mode.PHOTO
            setOnStateChangeListener(listener)
        }
    }

    @Test
    fun onHoldNotExpanding() {
        onView(withId(buttonId()))
                .perform(pressAndHold())
                .perform(waitFor(expandDelay() + expandDuration()))
                .perform(release())

        verify(listener, times(2)).onStateChanged(stateCaptor.capture())

        assertEquals(CameraButton.State.PRESSED, stateCaptor.allValues[0])
        assertEquals(CameraButton.State.DEFAULT, stateCaptor.allValues[1])
    }

}