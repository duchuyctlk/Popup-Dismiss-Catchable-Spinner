Popup-Dismiss-Catchable-Spinner
===============================


Description
--
A custom `Spinner` to which you could set listener for popup dismissal event

Usage
--
Copy class `PopupDismissCatchableSpinner` to your src folder.

In layout files, use `com.duchuyctlk.widget.PopupDismissCatchableSpinner` instead of `Spinner` widget.

```xml
<com.duchuyctlk.widget.PopupDismissCatchableSpinner
  android:id="@+id/spinner1"
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  android:layout_centerInParent="true"
  android:layout_marginTop="@dimen/activity_vertical_margin"
  android:entries="@array/item_arrays"
  android:prompt="@string/spinner_prompt"
  android:spinnerMode="dropdown" />
```

Implement `PopupDismissListener` interface.

```java
public class MainActivity extends Activity implements PopupDismissListener {
	// TODO override interface's methods
	@Override
	public void onDismiss(DialogInterface dialog) {
		// you could process drop down case and dialog case seperately if needed
	}

	@Override
	public void onShow() {
		// ...
	}
}
```

Call `PopupDismissCatchableSpinner.addOnPopupDismissListener(PopupDismissListener listener)` and you are able to catch show/dismiss events

```java
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// ...
		spinner1 = (PopupDismissCatchableSpinner) findViewById(R.id.spinner1);
		spinner1.addOnPopupDismissListener(this);		
		// ...
	}
```

Status on CircleCI:
[![Circle CI](https://circleci.com/gh/duchuyctlk/Popup-Dismiss-Catchable-Spinner.svg?style=svg)](https://circleci.com/gh/duchuyctlk/Popup-Dismiss-Catchable-Spinner)

Test coverage on Coveralls.io:
[![Coverage Status](https://coveralls.io/repos/github/duchuyctlk/Popup-Dismiss-Catchable-Spinner/badge.svg?branch=master)](https://coveralls.io/github/duchuyctlk/Popup-Dismiss-Catchable-Spinner?branch=master)

