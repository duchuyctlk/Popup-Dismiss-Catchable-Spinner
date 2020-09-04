Popup-Dismiss-Catchable-Spinner
===============================


Description
--
A custom `Spinner` to which you could set listener for popup dismissal event.

Download
--
Insert these lines into your Module's `build.gradle` file.
```gradle
repositories {
    jcenter()
}

dependencies {
    compile 'com.duchuyctlk.widget:spinner-lib:0.0.3'
}
```

Usage
--
Follow the Download step.

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
	// TODO override interface's method
	@Override
	public void onDismiss(DialogInterface dialog) {
		// you could process drop down case and dialog case seperately if needed
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

License
--

    Copyright 2016 Nguyen Duc Huy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

Status on CircleCI:
[![Circle CI](https://circleci.com/gh/duchuyctlk/Popup-Dismiss-Catchable-Spinner.svg?style=svg)](https://circleci.com/gh/duchuyctlk/Popup-Dismiss-Catchable-Spinner)

Test coverage on Coveralls.io:
[![Coverage Status](https://coveralls.io/repos/github/duchuyctlk/Popup-Dismiss-Catchable-Spinner/badge.svg?branch=master)](https://coveralls.io/github/duchuyctlk/Popup-Dismiss-Catchable-Spinner?branch=master)
