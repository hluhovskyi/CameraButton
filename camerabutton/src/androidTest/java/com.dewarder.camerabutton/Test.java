package com.dewarder.camerabutton;

import android.os.SystemClock;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
public class Test {

    @Rule
    public final ActivityTestRule<CameraButtonActivity> rule = new ActivityTestRule<>(CameraButtonActivity.class);

    private CountingIdlingResource res = new CountingIdlingResource("dasd");

    @Before
    public void s() {
    }

    @org.junit.Test
    public void test() {
        onView(withId(android.R.id.message)).perform(LowLevelActions.pressAndHold());
        rule.getActivity();
        SystemClock.sleep(20000);

    }
}
