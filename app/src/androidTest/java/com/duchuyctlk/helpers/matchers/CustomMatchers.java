package com.duchuyctlk.helpers.matchers;

import android.view.View;
import android.widget.Spinner;

import com.duchuyctlk.Constant;
import com.duchuyctlk.widget.PopupDismissCatchableSpinner;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by huy.duc on Apr 8th 2016
 */
public class CustomMatchers {

    public static Matcher<View> withSpinnerMode(final int expectedMode) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof PopupDismissCatchableSpinner)) {
                    return false;
                }

                PopupDismissCatchableSpinner spinner = (PopupDismissCatchableSpinner) view;
                return spinner.getSpinnerMode() == expectedMode;
            }

            @Override
            public void describeTo(Description description) {
                String strExpectedMode = expectedMode == Spinner.MODE_DIALOG ? Constant.STR_MODE_DIALOG :
                        (expectedMode == Spinner.MODE_DROPDOWN ? Constant.STR_MODE_DROPDOWN :
                                String.valueOf(-1));
                description.appendText("with spinner mode: " + strExpectedMode);
            }
        };
    }
}
