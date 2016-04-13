package com.duchuyctlk;

import com.duchuyctlk.widget.PopupDismissCatchableSpinner;
import com.duchuyctlk.widget.PopupDismissCatchableSpinner.PopupDismissListener;

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

public class MainActivity extends Activity implements PopupDismissListener {

    private TextView tvDismissCount;
    private TextView tvOpenCount;
    private PopupDismissCatchableSpinner mSpinnerDropdown;
    private PopupDismissCatchableSpinner mSpinnerDialog;
    private int mDismissCount;
    private int mOpenCount;

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

        tvDismissCount = (TextView) findViewById(R.id.tv_count_dismiss);
        tvOpenCount = (TextView) findViewById(R.id.tv_count_open);
        mSpinnerDropdown = (PopupDismissCatchableSpinner) findViewById(R.id.spinner_dropdown);
        mSpinnerDialog = (PopupDismissCatchableSpinner) findViewById(R.id.spinner_dialog);

        ToggleButton btnUseListener = (ToggleButton) findViewById(R.id.btn_use_listener);
        if (btnUseListener.isChecked()) {
            addListeners();
        }

        btnUseListener.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addListeners();
                } else {
                    removeListeners();
                }
            }
        });

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

    private void addListeners() {
        mSpinnerDropdown.addOnPopupDismissListener(this);
        mSpinnerDialog.addOnPopupDismissListener(this);
    }

    private void removeListeners() {
        mSpinnerDropdown.removeOnPopupDismissListener(this);
        mSpinnerDialog.removeOnPopupDismissListener(this);
    }
}
