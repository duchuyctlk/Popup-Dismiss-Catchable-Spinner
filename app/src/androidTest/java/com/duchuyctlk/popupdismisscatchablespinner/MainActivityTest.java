package com.duchuyctlk.popupdismisscatchablespinner;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void testUI() {
        testWidgetIsDisplayed(R.id.tv_label_close,
                R.id.tv_count_dismiss,
                R.id.spinner_dropdown,
                R.id.tv_label_open,
                R.id.tv_count_open);
        onView(withId(R.id.tv_count_open)).check(matches(withText(String.valueOf(0))));
        onView(withId(R.id.tv_count_dismiss)).check(matches(withText(String.valueOf(0))));
    }

    @Test
    public void clickSpinnerDropdown() {
        clickOnSpinner(R.id.spinner_dropdown, 1);
        clickOnSpinner(R.id.spinner_dialog, 2);
    }

    private void clickOnSpinner(int spinnerId, int expectedValue) {
        onView(withId(spinnerId)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.tv_count_open)).check(matches(withText(String.valueOf(expectedValue))));
        onView(withId(R.id.tv_count_dismiss)).check(matches(withText(String.valueOf(expectedValue))));
    }

    private void testWidgetIsDisplayed(int... ids) {
        for (int id : ids) {
            onView(withId(id)).check(matches(isDisplayed()));
        }
    }
}
