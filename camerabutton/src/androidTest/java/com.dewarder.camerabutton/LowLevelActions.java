package com.dewarder.camerabutton;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.MotionEvents;
import android.support.test.espresso.action.Press;
import android.view.MotionEvent;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;

public class LowLevelActions {
    static MotionEvent sMotionEventDownHeldView = null;

    public static PressAndHoldAction pressAndHold() {
        return new PressAndHoldAction();
    }

    public static ReleaseAction release() {
        return new ReleaseAction();
    }

    public static void tearDown() {
        sMotionEventDownHeldView = null;
    }

    static class PressAndHoldAction implements ViewAction {
        @Override
        public Matcher<View> getConstraints() {
            return isDisplayingAtLeast(90); // Like GeneralClickAction
        }

        @Override
        public String getDescription() {
            return "Press and hold action";
        }

        @Override
        public void perform(final UiController uiController, final View view) {
            if (sMotionEventDownHeldView != null) {
                throw new AssertionError("Only one view can be held at a time");
            }

            float[] precision = Press.FINGER.describePrecision();
            float[] coords = GeneralLocation.CENTER.calculateCoordinates(view);
            sMotionEventDownHeldView = MotionEvents.sendDown(uiController, coords, precision).down;
            // TODO: save view information and make sure release() is on same view
        }
    }

    static class ReleaseAction implements ViewAction {
        @Override
        public Matcher<View> getConstraints() {
            return isDisplayingAtLeast(90);  // Like GeneralClickAction
        }

        @Override
        public String getDescription() {
            return "Release action";
        }

        @Override
        public void perform(final UiController uiController, final View view) {
            if (sMotionEventDownHeldView == null) {
                throw new AssertionError("Before calling release(), you must call pressAndHold() on a view");
            }

            float[] coords = GeneralLocation.CENTER.calculateCoordinates(view);
            MotionEvents.sendUp(uiController, sMotionEventDownHeldView, coords);
        }
    }
}