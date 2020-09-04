package com.duchuyctlk.widget.internal

import android.content.DialogInterface
import android.widget.PopupWindow
import com.duchuyctlk.widget.PopupDismissCatchableSpinner

internal interface InternalListener : PopupWindow.OnDismissListener, DialogInterface.OnDismissListener {

    fun setPopupListener(listener: PopupWindow.OnDismissListener)

    fun addDismissListener(listener: PopupDismissCatchableSpinner.PopupDismissListener)

    fun addOpenListener(listener: PopupDismissCatchableSpinner.PopupOpenListener)

    fun removeDismissListener(listener: PopupDismissCatchableSpinner.PopupDismissListener)

    fun removeOpenListener(listener: PopupDismissCatchableSpinner.PopupOpenListener)

    fun onShow()
}
