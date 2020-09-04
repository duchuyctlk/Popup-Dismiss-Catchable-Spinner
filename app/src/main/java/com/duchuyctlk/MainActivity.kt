package com.duchuyctlk

import android.app.Activity
import android.app.KeyguardManager
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import com.duchuyctlk.widget.PopupDismissCatchableSpinner
import com.duchuyctlk.widget.PopupDismissCatchableSpinner.PopupDismissListener
import com.duchuyctlk.widget.PopupDismissCatchableSpinner.PopupOpenListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), PopupDismissListener, PopupOpenListener {

    private var mDismissCount = 0
    private var mOpenCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            val km = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            val keyguardLock = km.newKeyguardLock(MainActivity::class.java.simpleName)
            keyguardLock.disableKeyguard()
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
        setContentView(R.layout.activity_main)
        mDismissCount = 0
        mOpenCount = 0
        btnUseListener.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChanged(isChecked)
        }
        onCheckedChanged(btnUseListener.isChecked)
    }

    private fun onCheckedChanged(isChecked: Boolean) {
        if (isChecked) {
            addListeners()
        } else {
            removeListeners()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onDismiss(dialog: DialogInterface?) {
        mDismissCount++
        tvDismissCount.text = mDismissCount.toString()
    }

    override fun onShow() {
        mOpenCount++
        tvOpenCount.text = mOpenCount.toString()
    }

    private fun addListeners() {
        addSpinnerListener(spinnerDropdown, spinnerDialog)
    }

    private fun removeListeners() {
        removeSpinnerListener(spinnerDropdown, spinnerDialog)
    }

    private fun addSpinnerListener(vararg spinners: PopupDismissCatchableSpinner) {
        for (spinner in spinners) {
            spinner.addOnPopupDismissListener(this)
            spinner.addOnPopupOpenListener(this)
        }
    }

    private fun removeSpinnerListener(vararg spinners: PopupDismissCatchableSpinner) {
        for (spinner in spinners) {
            spinner.removeOnPopupDismissListener(this)
            spinner.removeOnPopupOpenListener(this)
        }
    }
}
