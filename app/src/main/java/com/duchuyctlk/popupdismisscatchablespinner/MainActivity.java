package com.duchuyctlk.popupdismisscatchablespinner;

import com.duchuyctlk.popupdismisscatchablespinner.widget.PopupDismissCatchableSpinner;
import com.duchuyctlk.popupdismisscatchablespinner.widget.PopupDismissCatchableSpinner.PopupDismissListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements PopupDismissListener {

    private TextView tvDismissCount;
    private TextView tvOpenCount;
    private int mDismissCount;
    private int mOpenCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDismissCount = (TextView) findViewById(R.id.tv_count_dismiss);
        tvOpenCount = (TextView) findViewById(R.id.tv_count_open);

        PopupDismissCatchableSpinner spinnerCountries = (PopupDismissCatchableSpinner)
                findViewById(R.id.spinner_countries);
        spinnerCountries.setOnPopupDismissListener(this);

        mDismissCount = 0;
        mOpenCount = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mDismissCount++;
        tvDismissCount.setText(String.valueOf(mDismissCount));
    }

    @Override
    public void onShow() {
        mOpenCount++;
        tvOpenCount.setText(String.valueOf(mOpenCount));
    }
}
