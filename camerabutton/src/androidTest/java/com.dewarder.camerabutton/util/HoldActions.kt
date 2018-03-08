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

package com.dewarder.camerabutton.util

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.GeneralLocation
import android.support.test.espresso.action.MotionEvents
import android.support.test.espresso.action.Press
import android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import android.view.MotionEvent
import android.view.View
import org.hamcrest.Matcher

private var sMotionEventDownHeldView: MotionEvent? = null

fun pressAndHold(): ViewAction = PressAndHoldAction()

fun release(): ViewAction = ReleaseAction()

fun disposeHoldActions() {
    sMotionEventDownHeldView = null
}

private class PressAndHoldAction : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isDisplayingAtLeast(90) // Like GeneralClickAction
    }

    override fun getDescription(): String {
        return "Press and hold action"
    }

    override fun perform(uiController: UiController, view: View) {
        if (sMotionEventDownHeldView != null) {
            throw AssertionError("Only one view can be held at a time")
        }

        val precision = Press.FINGER.describePrecision()
        val coords = GeneralLocation.CENTER.calculateCoordinates(view)
        sMotionEventDownHeldView = MotionEvents.sendDown(uiController, coords, precision).down
        // TODO: save view information and make sure release() is on same view
    }
}

private class ReleaseAction : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return isDisplayingAtLeast(90)  // Like GeneralClickAction
    }

    override fun getDescription(): String {
        return "Release action"
    }

    override fun perform(uiController: UiController, view: View) {
        if (sMotionEventDownHeldView == null) {
            throw AssertionError("Before calling release(), you must call pressAndHold() on a view")
        }

        val coords = GeneralLocation.CENTER.calculateCoordinates(view)
        MotionEvents.sendUp(uiController, sMotionEventDownHeldView, coords)
        sMotionEventDownHeldView = null
    }
}