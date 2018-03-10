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

import android.content.Context
import android.graphics.Color
import android.support.annotation.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class CameraButtonInitializationTest {

    @Test
    fun testMainCircleRadiusDefaultInitializedCorrect() {
        assertDefaultDimen(
                dimenProvider = CameraButton::getMainCircleRadius,
                dimenRes = R.dimen.cb_main_circle_radius_default)
    }

    @Test
    fun testMainCircleRadiusCustomInitializedCorrect() {
        assertCustomDimen(
                attr = R.attr.cb_main_circle_radius,
                dimenProvider = CameraButton::getMainCircleRadius)
    }

    @Test
    fun testMainCircleRadiusExpandedDefaultInitializedCorrect() {
        assertDefaultDimen(
                dimenProvider = CameraButton::getMainCircleRadiusExpanded,
                dimenRes = R.dimen.cb_main_circle_radius_expanded_default)
    }

    @Test
    fun testMainCircleRadiusExpandedCustomInitializedCorrect() {
        assertCustomDimen(
                attr = R.attr.cb_main_circle_radius_expanded,
                dimenProvider = CameraButton::getMainCircleRadiusExpanded)
    }

    @Test
    fun testStrokeWidthDefaultInitializedCorrect() {
        assertDefaultDimen(
                dimenProvider = CameraButton::getStrokeWidth,
                dimenRes = R.dimen.cb_stroke_width_default)
    }

    @Test
    fun testStrokeWidthCustomInitializedCorrect() {
        assertCustomDimen(
                attr = R.attr.cb_stroke_width,
                dimenProvider = CameraButton::getStrokeWidth)
    }

    @Test
    fun testProgressArcWidthDefaultInitializedCorrect() {
        assertDefaultDimen(
                dimenProvider = CameraButton::getProgressArcWidth,
                dimenRes = R.dimen.cb_progress_arc_width_default)
    }

    @Test
    fun testProgressArcWidthCustomInitializedCorrect() {
        assertCustomDimen(
                attr = R.attr.cb_progress_arc_width,
                dimenProvider = CameraButton::getProgressArcWidth)
    }

    @Test
    fun testMainCircleColorDefaultInitializedCorrect() {
        assertDefaultColor(
                colorProvider = CameraButton::getMainCircleColor,
                colorRes = R.color.cb_main_circle_color_default)
    }

    @Test
    fun testMainCircleColorCustomInitializedCorrect() {
        assertCustomColor(
                attr = R.attr.cb_main_circle_color,
                colorProvider = CameraButton::getMainCircleColor)
    }


    @Test
    fun testMainCircleColorPressedDefaultInitializedCorrect() {
        assertDefaultColor(
                colorProvider = CameraButton::getMainCircleColorPressed,
                colorRes = R.color.cb_main_circle_color_pressed_default)
    }

    @Test
    fun testMainCircleColorPressedCustomInitializedCorrect() {
        assertCustomColor(
                attr = R.attr.cb_main_circle_color_pressed,
                colorProvider = CameraButton::getMainCircleColorPressed)
    }

    @Test
    fun testStrokeColorDefaultInitializedCorrect() {
        assertDefaultColor(
                colorProvider = CameraButton::getStrokeColor,
                colorRes = R.color.cb_stroke_color_default)
    }

    @Test
    fun testStrokeColorCustomInitializedCorrect() {
        assertCustomColor(
                attr = R.attr.cb_stroke_color,
                colorProvider = CameraButton::getStrokeColor)
    }

    @Test
    fun testStrokeColorPressedDefaultInitializedCorrect() {
        assertDefaultColor(
                colorProvider = CameraButton::getStrokeColorPressed,
                colorRes = R.color.cb_stroke_color_pressed_default)
    }

    @Test
    fun testStrokeColorPressedCustomInitializedCorrect() {
        assertCustomColor(
                attr = R.attr.cb_stroke_color_pressed,
                colorProvider = CameraButton::getStrokeColorPressed)
    }

    @Test
    fun testExpandDurationDefaultInitializedCorrect() {
        assertDefaultDuration(
                durationProvider = CameraButton::getExpandDuration,
                durationRes = R.integer.cb_expand_duration_default)
    }

    @Test
    fun testExpandDurationCustomInitializedCorrect() {
        assertCustomDuration(
                attr = R.attr.cb_expand_duration,
                durationProvider = CameraButton::getExpandDuration)
    }

    @Test
    fun testExpandDelayDefaultInitializedCorrect() {
        assertDefaultDuration(
                durationProvider = CameraButton::getExpandDelay,
                durationRes = R.integer.cb_expand_delay_default)
    }

    @Test
    fun testExpandDelayCustomInitializedCorrect() {
        assertCustomDuration(
                attr = R.attr.cb_expand_delay,
                durationProvider = CameraButton::getExpandDelay)
    }

    @Test
    fun testCollapseDurationDefaultInitializedCorrect() {
        assertDefaultDuration(
                durationProvider = CameraButton::getCollapseDuration,
                durationRes = R.integer.cb_collapse_duration_default)
    }

    @Test
    fun testCollapseDurationCustomInitializedCorrect() {
        assertCustomDuration(
                attr = R.attr.cb_collapse_duration,
                durationProvider = CameraButton::getCollapseDuration)
    }

    @Test
    fun testHoldDurationDefaultInitializedCorrect() {
        assertDefaultDuration(
                durationProvider = CameraButton::getHoldDuration,
                durationRes = R.integer.cb_hold_duration_default)
    }

    @Test
    fun testHoldDurationCustomInitializedCorrect() {
        assertCustomDuration(
                attr = R.attr.cb_hold_duration,
                durationProvider = CameraButton::getHoldDuration)
    }

    @Test
    fun testIconSizeDefaultInitializedCorrect() {
        assertDefaultDimen(
                dimenProvider = CameraButton::getIconSize,
                dimenRes = R.dimen.cb_icon_size_default)
    }

    @Test
    fun testIconSizeCustomInitializedCorrect() {
        assertCustomDimen(
                attr = R.attr.cb_icon_size,
                dimenProvider = CameraButton::getIconSize)
    }

    @Test
    fun testModeDefaultInitializedCorrect() {
        val button = CameraButton(context)
        assertEquals(button.mode, CameraButton.Mode.ALL)
    }

    @Test
    fun testModeAllCustomInitializedCorrect() {
        val attrs = createAttrs(R.attr.cb_mode, "all")
        val button = CameraButton(context, attrs)
        assertEquals(button.mode, CameraButton.Mode.ALL)
    }

    @Test
    fun testModeTapCustomInitializedCorrect() {
        val attrs = createAttrs(R.attr.cb_mode, "tap")
        val button = CameraButton(context, attrs)
        assertEquals(button.mode, CameraButton.Mode.TAP)
    }

    @Test
    fun testModeHoldCustomInitializedCorrect() {
        val attrs = createAttrs(R.attr.cb_mode, "hold")
        val button = CameraButton(context, attrs)
        assertEquals(button.mode, CameraButton.Mode.HOLD)
    }

    @Test
    fun testCollapseActionDefaultInitializedCorrect() {
        val button = CameraButton(context)
        assertEquals(button.collapseAction, CameraButton.Action.RELEASE)
    }

    @Test
    fun testCollapseActionReleaseCustomInitializedCorrect() {
        val attrs = createAttrs(R.attr.cb_collapse_action, "release")
        val button = CameraButton(context, attrs)
        assertEquals(button.collapseAction, CameraButton.Action.RELEASE)
    }

    @Test
    fun testCollapseActionClickCustomInitializedCorrect() {
        val attrs = createAttrs(R.attr.cb_collapse_action, "click")
        val button = CameraButton(context, attrs)
        assertEquals(button.collapseAction, CameraButton.Action.CLICK)
    }

    private fun assertDefaultDimen(dimenProvider: (CameraButton) -> Int, @DimenRes dimenRes: Int) {
        val button = CameraButton(context)
        assertEquals(dimenProvider(button), context.resources.getDimensionPixelSize(dimenRes))
    }

    private fun assertCustomDimen(@AttrRes attr: Int, dimenProvider: (CameraButton) -> Int) {
        val dp = 1
        val attrs = createAttrs(attr, "${dp}dp")
        val button = CameraButton(context, attrs)
        assertEquals(dimenProvider(button), dp)
    }

    private fun assertDefaultColor(colorProvider: (CameraButton) -> Int, @ColorRes colorRes: Int) {
        val button = CameraButton(context)
        assertEquals(colorProvider(button), context.getColor(colorRes))
    }

    private fun assertCustomColor(@ArrayRes attr: Int, colorProvider: (CameraButton) -> Int) {
        val color = "#000000"
        val attrs = createAttrs(attr, color)
        val button = CameraButton(context, attrs)
        assertEquals(colorProvider(button), Color.parseColor(color))
    }

    private fun assertDefaultDuration(durationProvider: (CameraButton) -> Long, @IntegerRes durationRes: Int) {
        val button = CameraButton(context)
        assertEquals(durationProvider(button).toInt(), context.resources.getInteger(durationRes))
    }

    private fun assertCustomDuration(@AttrRes attr: Int, durationProvider: (CameraButton) -> Long) {
        val duration = 1L
        val attrs = createAttrs(attr, duration.toString())
        val button = CameraButton(context, attrs)
        assertEquals(durationProvider(button), duration)
    }

    private companion object {

        val context: Context
            get() = RuntimeEnvironment.application

        fun createAttrs(@AttrRes attr: Int, value: String) =
                Robolectric.buildAttributeSet()
                        .addAttribute(attr, value)
                        .build()
    }
}