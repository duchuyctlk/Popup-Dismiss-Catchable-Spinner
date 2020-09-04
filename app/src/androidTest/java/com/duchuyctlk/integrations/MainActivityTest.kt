package com.duchuyctlk.integrations

import android.widget.Spinner
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.duchuyctlk.MainActivity
import com.duchuyctlk.R
import com.duchuyctlk.helpers.matchers.CustomMatchers.withSpinnerMode
import com.duchuyctlk.widget.PopupDismissCatchableSpinner
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun testUI() {
        testWidgetIsDisplayed(R.id.tv_label_close,
                R.id.tvDismissCount,
                R.id.spinnerDropdown,
                R.id.tvLabelOpen,
                R.id.tvOpenCount,
                R.id.btnUseListener)
        checkTextViewsValue(0, R.id.tvOpenCount, R.id.tvDismissCount)
    }

    @Ignore("Fix after this commit")
    @Test
    fun clickSpinnerDropdown() {
        clickOnSpinner(R.id.spinnerDropdown, 1)
        clickOnSpinner(R.id.spinnerDialog, 2)
    }

    @Test
    fun checkSpinnerMode() {
        checkSpinnerMode(R.id.spinnerDialog, Spinner.MODE_DIALOG)
        checkSpinnerMode(R.id.spinnerDropdown, Spinner.MODE_DROPDOWN)
    }

    @Ignore("Fix after this commit")
    @Test
    fun clickOnToggleButton() {
        // turn off listener
        onView(withId(R.id.btnUseListener)).perform(click())
        clickOnSpinner(R.id.spinnerDropdown, 0)
        clickOnSpinner(R.id.spinnerDialog, 0)

        // turn on listener
        onView(withId(R.id.btnUseListener)).perform(click())
        clickSpinnerDropdown()
    }

    @Ignore("Will move to unit test")
    @Test
    fun handleExceptions() {
        val activity = mActivityRule.activity
        val spinner: PopupDismissCatchableSpinner = activity.findViewById(R.id.spinnerDialog)
        val spySpinner = Mockito.spy(spinner)
        Mockito.doThrow(RuntimeException()).`when`(spySpinner).isFieldSpinnerPopupNull
        spySpinner.spinnerMode
        spySpinner.performClick()
    }

    private fun clickOnSpinner(spinnerId: Int, expectedValue: Int) {
        onView(withId(spinnerId)).perform(click())
        pressBack()
        checkTextViewsValue(expectedValue, R.id.tvOpenCount, R.id.tvDismissCount)
    }

    private fun testWidgetIsDisplayed(vararg ids: Int) {
        for (id in ids) {
            onView(withId(id)).check(matches(isDisplayed()))
        }
    }

    private fun checkSpinnerMode(spinnerId: Int, spinnerMode: Int) {
        onView(withId(spinnerId)).check(matches(withSpinnerMode(spinnerMode)))
    }

    private fun checkTextViewsValue(expectedValue: Int, vararg textViewIds: Int) {
        for (textViewId in textViewIds) {
            onView(withId(textViewId)).check(matches(withText(expectedValue.toString())))
        }
    }
}
