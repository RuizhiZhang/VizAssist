//integration test: using espresso package, 测试一部分应用，可能会结合多个class
package com.example.vizassist;

import android.content.Context;
import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class); //先让mainActivity运行起来，运行在前端

    @Test
    public void useAppContext() { //这个测试随着创建project自动生成，目的是check name一不一致
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.vizassist", appContext.getPackageName());
    }

    @Test
    public void viewsAreVisible() {
        onView(withId(R.id.capturedImage)).check(matches((isDisplayed())));
        onView(withId(R.id.resultView)).check(matches((isDisplayed())));
        onView(withId(R.id.resultView)).check(matches(withText(R.string.result_placeholder)));
        onView(withId(R.id.capturedImage)).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                assertNotEquals(view.getHeight(), 0);
                assertNotEquals(view.getWidth(), 0);
            }
        });
    }
}
