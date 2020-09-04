package com.duchuyctlk.widget.internal

import android.app.AlertDialog
import android.widget.ListPopupWindow
import android.widget.PopupWindow
import android.widget.Spinner
import com.duchuyctlk.widget.internal.Constants.M_POPUP
import com.duchuyctlk.widget.internal.Constants.ON_DISMISS_LISTENER

internal interface ListenerSubstitutorFactory {

    fun getListenerSubstitutor(mode: Int): ListenerSubstitutor
}

internal class ListenerSubstitutorFactoryImpl(private val mInternalListener: InternalListener) : ListenerSubstitutorFactory {

    private class DialogListenerSubstitutor(private val mInternalListener: InternalListener) : ListenerSubstitutor {

        override fun substitute(spinnerPopup: Any?) {
            // reflecting DialogPopup.mPopup
            spinnerPopup?.javaClass?.getDeclaredField(M_POPUP)?.let { fieldAlertDialog ->
                // enable access checks to Spinner.mPopup.mPopup
                fieldAlertDialog.isAccessible = true

                // set onDismissListener
                (fieldAlertDialog.get(spinnerPopup) as AlertDialog).setOnDismissListener(mInternalListener)

                // disable access checks to Spinner.mPopup.mPopup
                fieldAlertDialog.isAccessible = false
            }
        }
    }

    private class DropDownSubstitutor(private val mInternalListener: InternalListener) : ListenerSubstitutor {

        override fun substitute(spinnerPopup: Any?) {
            // reflecting Spinner.mPopup.mPopup
            ListPopupWindow::class.java.getDeclaredField(M_POPUP).let { fieldPopupWindow ->
                fieldPopupWindow.isAccessible = true

                // reflecting Spinner.mPopup.mPopup.OnDismissListener
                PopupWindow::class.java.getDeclaredField(ON_DISMISS_LISTENER).let { fieldOnDismissListener ->
                    fieldOnDismissListener.isAccessible = true

                    // get the default listener and set into the custom listener
                    (fieldOnDismissListener.get(fieldPopupWindow.get(spinnerPopup)) as? PopupWindow.OnDismissListener)?.let { defaultListener ->
                        mInternalListener.setPopupListener(defaultListener)

                        // replace the default listener by the custom one
                        (spinnerPopup as? ListPopupWindow)?.setOnDismissListener(mInternalListener)
                    }

                    fieldOnDismissListener.isAccessible = false
                }

                fieldPopupWindow.isAccessible = false
            }
        }
    }

    private class DefaultSubstitutor : ListenerSubstitutor {

        override fun substitute(spinnerPopup: Any?) {
        }
    }

    override fun getListenerSubstitutor(mode: Int): ListenerSubstitutor {
        return when (mode) {
            Spinner.MODE_DIALOG -> DialogListenerSubstitutor(mInternalListener)
            Spinner.MODE_DROPDOWN -> DropDownSubstitutor(mInternalListener)
            else -> DefaultSubstitutor()
        }
    }
}
