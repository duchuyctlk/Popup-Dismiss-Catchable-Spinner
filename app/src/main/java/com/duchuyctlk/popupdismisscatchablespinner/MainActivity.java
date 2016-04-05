package com.duchuyctlk.popupdismisscatchablespinner;

import com.duchuyctlk.popupdismisscatchablespinner.widget.PopupDismissCatchableSpinner;
import com.duchuyctlk.popupdismisscatchablespinner.widget.PopupDismissCatchableSpinner.PopupDismissListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements PopupDismissListener {

	private TextView txtDismissCount;
	private TextView txtOpenCount;
	private PopupDismissCatchableSpinner spinnerCountries;
	private int dismissCount;
	private int openCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtDismissCount = (TextView) findViewById(R.id.txt_count_dismiss);
		txtOpenCount = (TextView) findViewById(R.id.txt_count_open);
		
		spinnerCountries = (PopupDismissCatchableSpinner) findViewById(R.id.spinner_countries);
		spinnerCountries.setOnPopupDismissListener(this);
		
		dismissCount = 0;
		openCount = 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// case dialog popup
		handlePopupDismissEvent();
	}

	@Override
	public void onShow() {
		openCount ++;
		txtOpenCount.setText(String.valueOf(openCount));
	}

	private void handlePopupDismissEvent() {
		dismissCount ++;
		txtDismissCount.setText(String.valueOf(dismissCount));
	}
}
