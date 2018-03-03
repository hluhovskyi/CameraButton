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

import android.support.annotation.CallSuper
import android.support.test.rule.ActivityTestRule
import com.dewarder.camerabutton.util.disposeHoldActions
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseStateTest {

    @get:Rule
    val activityRule: ActivityTestRule<CameraButtonActivity>
            = ActivityTestRule(CameraButtonActivity::class.java)

    @Before
    @CallSuper
    open fun setUp() {
        activityRule.activity.button.apply {
            id = BUTTON_ID
            expandDelay = EXPAND_DELAY
            expandDuration = EXPAND_DURATION
            collapseDuration = COLLAPSE_DURATION
        }
    }

    @After
    fun tearDown() {
        disposeHoldActions()
    }


    companion object {
        private const val BUTTON_ID = android.R.id.button1

        private const val EXPAND_DELAY = 200L
        private const val EXPAND_DURATION = 400L
        private const val COLLAPSE_DURATION = 400L

        fun buttonId() = BUTTON_ID

        fun expandDelay() = EXPAND_DELAY

        fun expandDuration() = EXPAND_DURATION

        fun collapseDuration() = COLLAPSE_DURATION
    }
}