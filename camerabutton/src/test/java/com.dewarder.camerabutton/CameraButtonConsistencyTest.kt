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

import android.app.Activity
import android.graphics.Canvas
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class CameraButtonConsistencyTest {

    private lateinit var canvas: Canvas
    private lateinit var button: CameraButton

    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    @Before
    fun setUp() {
        canvas = Canvas()
        Shadows.shadowOf(canvas).apply {
            width = CANVAS_SIZE
            height = CANVAS_SIZE
        }

        val activity = Robolectric.buildActivity(Activity::class.java).get()
        button = CameraButton(activity).apply {
            mainCircleRadius = MAIN_CIRCLE_RADIUS
            strokeWidth = STROKE_WIDTH
            mainCircleRadiusExpanded = MAIN_CIRCLE_RADIUS_EXPANDED
            progressArcWidth = PROGRESS_ARC_WIDTH
        }
    }

    @Test
    fun testConsistencyValid() {
        button.onDraw(canvas)
    }

    @Test
    fun testConsistencyMainCircleRadiusInvalid() {
        exceptionRule.expect(ConsistencyValidationException::class.java)
        exceptionRule.expectMessage("MainCircleRadius")

        button.mainCircleRadius = GREATER_THAN_CANVAS_SIZE
        button.onDraw(canvas)
    }

    @Test
    fun testConsistencyStrokeWidthInvalid() {
        exceptionRule.expect(ConsistencyValidationException::class.java)
        exceptionRule.expectMessage("StrokeWidth")

        button.strokeWidth = GREATER_THAN_CANVAS_SIZE
        button.onDraw(canvas)
    }

    @Test
    fun testConsistencyMainCircleRadiusExpandedInvalid() {
        exceptionRule.expect(ConsistencyValidationException::class.java)
        exceptionRule.expectMessage("MainCircleRadiusExpanded")

        button.mainCircleRadiusExpanded = GREATER_THAN_CANVAS_SIZE
        button.onDraw(canvas)
    }

    @Test
    fun testConsistencyProgressArcInvalid() {
        exceptionRule.expect(ConsistencyValidationException::class.java)
        exceptionRule.expectMessage("ProgressArcWidth")

        button.progressArcWidth = GREATER_THAN_CANVAS_SIZE
        button.onDraw(canvas)
    }

    @Test
    fun testConsistencyInvalidButNotValidated() {
        button.apply {
            mainCircleRadius = GREATER_THAN_CANVAS_SIZE
            mainCircleRadiusExpanded = GREATER_THAN_CANVAS_SIZE
            strokeWidth = GREATER_THAN_CANVAS_SIZE
            progressArcWidth = GREATER_THAN_CANVAS_SIZE
            setShouldCheckConsistency(false)
        }

        button.onDraw(canvas)
    }

    companion object {

        private const val CANVAS_SIZE = 100
        private const val GREATER_THAN_CANVAS_SIZE = CANVAS_SIZE + 1

        private const val MAIN_CIRCLE_RADIUS = 10
        private const val STROKE_WIDTH = 10
        private const val MAIN_CIRCLE_RADIUS_EXPANDED = 10
        private const val PROGRESS_ARC_WIDTH = 10
    }
}