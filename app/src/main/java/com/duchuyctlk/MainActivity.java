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
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class MainActivity extends Activity implements PopupDismissListener {

    @Bind(R.id.tv_count_dismiss)
    TextView tvDismissCount;
    @Bind(R.id.tv_count_open)
    TextView tvOpenCount;
    @Bind(R.id.spinner_dropdown)
    PopupDismissCatchableSpinner mSpinnerDropdown;
    @Bind(R.id.spinner_dialog)
    PopupDismissCatchableSpinner mSpinnerDialog;
    @Bind(R.id.btn_use_listener)
    ToggleButton btnUseListener;

    private int mDismissCount;
    private int mOpenCount;

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
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
        ButterKnife.bind(this);

        mDismissCount = 0;
        mOpenCount = 0;

        onCheckedChanged(btnUseListener.isChecked());
    }

    @OnCheckedChanged(value = {R.id.btn_use_listener})
    void onCheckedChanged(boolean isChecked) {
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
        mSpinnerDropdown.addOnPopupDismissListener(this);
        mSpinnerDialog.addOnPopupDismissListener(this);
    }

    private void removeListeners() {
        mSpinnerDropdown.removeOnPopupDismissListener(this);
        mSpinnerDialog.removeOnPopupDismissListener(this);
    }
}
