package com.duchuyctlk;

import com.duchuyctlk.widget.PopupDismissCatchableSpinner;
import com.duchuyctlk.widget.PopupDismissCatchableSpinner.PopupDismissListener;
import com.duchuyctlk.widget.PopupDismissCatchableSpinner.PopupOpenListener;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements PopupDismissListener, PopupOpenListener {

    TextView tvDismissCount;
    TextView tvOpenCount;
    PopupDismissCatchableSpinner mSpinnerDropdown;
    PopupDismissCatchableSpinner mSpinnerDialog;
    ToggleButton btnUseListener;

    private int mDismissCount;
    private int mOpenCount;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock = km.newKeyguardLock(MainActivity.class.getSimpleName());
            keyguardLock.disableKeyguard();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

        setContentView(R.layout.activity_main);

        mDismissCount = 0;
        mOpenCount = 0;

        tvDismissCount = (TextView) findViewById(R.id.tv_count_dismiss);
        tvOpenCount = (TextView) findViewById(R.id.tv_count_open);
        mSpinnerDropdown = (PopupDismissCatchableSpinner) findViewById(R.id.spinner_dropdown);
        mSpinnerDialog = (PopupDismissCatchableSpinner) findViewById(R.id.spinner_dialog);
        btnUseListener = (ToggleButton) findViewById(R.id.btn_use_listener);

        btnUseListener.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked)
                -> onCheckedChanged(isChecked));

        onCheckedChanged(btnUseListener.isChecked());
    }

    private void onCheckedChanged(boolean isChecked) {
        if (isChecked) {
            addListeners();
        } else {
            removeListeners();
        }
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

    private void addListeners() {
        addSpinnerListener(mSpinnerDropdown, mSpinnerDialog);
    }

    private void removeListeners() {
        removeSpinnerListener(mSpinnerDropdown, mSpinnerDialog);
    }

    private void addSpinnerListener(PopupDismissCatchableSpinner... spinners) {
        for (PopupDismissCatchableSpinner spinner : spinners) {
            spinner.addOnPopupDismissListener(this);
            spinner.addOnPopupOpenListener(this);
        }
    }

    private void removeSpinnerListener(PopupDismissCatchableSpinner... spinners) {
        for (PopupDismissCatchableSpinner spinner : spinners) {
            spinner.removeOnPopupDismissListener(this);
            spinner.removeOnPopupOpenListener(this);
        }
    }
}
