package com.duchuyctlk;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Spinner;

import com.duchuyctlk.helpers.matchers.CustomMatchers;
import com.duchuyctlk.popupdismisscatchablespinner.R;

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
                R.id.tv_count_open,
                R.id.btn_use_listener);
        checkTextViewsValue(0, R.id.tv_count_open, R.id.tv_count_dismiss);
    }

    @Test
    public void clickSpinnerDropdown() {
        clickOnSpinner(R.id.spinner_dropdown, 1);
        clickOnSpinner(R.id.spinner_dialog, 2);
    }

    @Test
    public void checkSpinnerMode() {
        checkSpinnerMode(R.id.spinner_dialog, Spinner.MODE_DIALOG);
        checkSpinnerMode(R.id.spinner_dropdown, Spinner.MODE_DROPDOWN);
    }

    @Test
    public void clickOnToggleButton() {
        // turn off listener
        onView(withId(R.id.btn_use_listener)).perform(click());
        clickOnSpinner(R.id.spinner_dropdown, 0);
        clickOnSpinner(R.id.spinner_dialog, 0);

        // turn on listener
        onView(withId(R.id.btn_use_listener)).perform(click());
        clickSpinnerDropdown();

    }

    private void clickOnSpinner(int spinnerId, int expectedValue) {
        onView(withId(spinnerId)).perform(click());
        Espresso.pressBack();
        checkTextViewsValue(expectedValue, R.id.tv_count_open, R.id.tv_count_dismiss);
    }

    private void testWidgetIsDisplayed(int... ids) {
        for (int id : ids) {
            onView(withId(id)).check(matches(isDisplayed()));
        }
    }

    private void checkSpinnerMode(int spinnerId, int spinnerMode) {
        onView(withId(spinnerId)).check(matches(CustomMatchers.withSpinnerMode(spinnerMode)));
    }

    private void checkTextViewsValue(int expectedValue, int... textViewIds) {
        for (int textViewId : textViewIds) {
            onView(withId(textViewId)).check(matches(withText(String.valueOf(expectedValue))));
        }
    }
}
