package com.duchuyctlk.helpers.matchers

import android.view.View
import android.widget.Spinner.MODE_DIALOG
import android.widget.Spinner.MODE_DROPDOWN
import com.duchuyctlk.Constant.STR_MODE_DIALOG
import com.duchuyctlk.Constant.STR_MODE_DROPDOWN
import com.duchuyctlk.widget.PopupDismissCatchableSpinner
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object CustomMatchers {
    fun withSpinnerMode(expectedMode: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(view: View?): Boolean {
                if (view !is PopupDismissCatchableSpinner) {
                    return false
                }
                return view.spinnerMode == expectedMode
            }

            override fun describeTo(description: Description) {
                val strExpectedMode = when (expectedMode) {
                    MODE_DIALOG -> STR_MODE_DIALOG
                    MODE_DROPDOWN -> STR_MODE_DROPDOWN
                    else -> (-1).toString()
                }
                description.appendText("with spinner mode: $strExpectedMode")
            }
        }
    }
}
