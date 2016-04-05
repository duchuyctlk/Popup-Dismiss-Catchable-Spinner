Popup-Dismiss-Catchable-Spinner
===============================


Description
--
A custom Spinner to which you could set listener for popup dismissal event

Usage
--
Copy com.duchuyctlk.popupdismisscatchablespinner.widget.PopupDismissCatchableSpinner.java to your src folder.

In layout files, use "com.duchuyctlk.popupdismisscatchablespinner.widget.PopupDismissCatchableSpinner" instead of Spinner widget.

```xml
<com.duchuyctlk.popupdismisscatchablespinner.widget.PopupDismissCatchableSpinner
  android:id="@+id/spinner1"
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  android:layout_centerInParent="true"
  android:layout_marginTop="@dimen/activity_vertical_margin"
  android:entries="@array/item_arrays"
  android:prompt="@string/spinner_prompt"
  android:spinnerMode="dropdown" />
```

Implement PopupDismissListener interface.

```java
public class MainActivity extends Activity implements PopupDismissListener {
	// TODO override interface's methods
	@Override
	public void onDismiss() {
		// case drop down popup
		handlePopupDismissEvent();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// case dialog popup
		handlePopupDismissEvent();
	}

	@Override
	public void onShow() {
		// ...
	}

	private void handlePopupDismissEvent() {
		// TODO you could process drop down case and dialog case seperately if needed
	}
}
```

Call PopupDismissCatchableSpinner.setOnPopupDismissListener(PopupDismissListener listener) and you are able to catch show/dismiss events

```java
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// ...
		spinner1 = (PopupDismissCatchableSpinner) findViewById(R.id.spinner1);
		spinner1.setOnPopupDismissListener(this);		
		// ...
	}
```
