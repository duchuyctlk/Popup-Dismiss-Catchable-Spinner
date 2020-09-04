package com.duchuyctlk.widget

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.widget.PopupWindow
import android.widget.Spinner
import com.duchuyctlk.widget.internal.Constants.DIALOG_POPUP
import com.duchuyctlk.widget.internal.Constants.DROPDOWN_POPUP
import com.duchuyctlk.widget.internal.Constants.IS_SHOWING
import com.duchuyctlk.widget.internal.Constants.MODE_UNKNOWN
import com.duchuyctlk.widget.internal.Constants.M_POPUP
import com.duchuyctlk.widget.internal.InternalListener
import com.duchuyctlk.widget.internal.ListenerSubstitutorFactory
import com.duchuyctlk.widget.internal.ListenerSubstitutorFactoryImpl
import com.duchuyctlk.widget.internal.addIfNotContain
import java.lang.reflect.Field

/**
 * Construct a new spinner with the given context's theme and the supplied attribute set.
 *
 * @param context The Context the view is running in, through which it can
 * access the current theme, resources, etc.
 * @param attrs   The attributes of the XML tag that is inflating the view.
 */
class PopupDismissCatchableSpinner(context: Context?, attrs: AttributeSet?) : Spinner(context, attrs) {

    /**
     * Interface used to allow the creator of a `[Spinner]`
     * to run some code when the Spinner's Popup is dismissed.
     */
    interface PopupDismissListener {
        fun onDismiss(dialog: DialogInterface? = null)
    }

    /**
     * Interface used to allow the creator of a `[Spinner]`
     * to run some code when the Spinner's Popup is opened.
     */
    interface PopupOpenListener {
        fun onShow()
    }

    private inner class InternalListenerImpl : InternalListener {

        private val mDismissListeners = mutableListOf<PopupDismissListener>()
        private val mOpenListeners = mutableListOf<PopupOpenListener>()
        private var mPopupListener: PopupWindow.OnDismissListener? = null

        override fun addDismissListener(listener: PopupDismissListener) {
            mDismissListeners.addIfNotContain(listener)
        }

        override fun addOpenListener(listener: PopupOpenListener) {
            mOpenListeners.addIfNotContain(listener)
        }

        override fun removeDismissListener(listener: PopupDismissListener) {
            mDismissListeners.remove(listener)
        }

        override fun removeOpenListener(listener: PopupOpenListener) {
            mOpenListeners.remove(listener)
        }

        override fun setPopupListener(listener: PopupWindow.OnDismissListener) {
            mPopupListener = listener
        }

        override fun onShow() {
            for (listener in mOpenListeners) {
                listener.onShow()
            }
        }

        override fun onDismiss(dialog: DialogInterface) {
            onDismissInternal(dialog)
        }

        override fun onDismiss() {
            onDismissInternal()
        }

        private fun onDismissInternal(dialog: DialogInterface? = null) {
            for (listener in mDismissListeners) {
                listener.onDismiss(dialog)
            }
            mPopupListener?.onDismiss()
            mIsPopupShown = false
        }
    }

    private var mIsPopupShown = false
    private var mFieldSpinnerPopup: Field? = null
    private val mInternalListener: InternalListener = InternalListenerImpl()
    private val mListenerSubstitutorFactory: ListenerSubstitutorFactory = ListenerSubstitutorFactoryImpl(mInternalListener)

    /**
     * Add a listener to receive a callback when the popup is opened or dismissed.
     *
     * @param listener Listener that will be notified when the popup is opened or dismissed.
     */
    fun addOnPopupDismissListener(listener: PopupDismissListener) {
        mInternalListener.addDismissListener(listener)
    }

    /**
     * Remove a listener from the list of this `[Spinner]`'s listeners
     *
     * @param listener Listener that will be removed from the list.
     */
    fun removeOnPopupDismissListener(listener: PopupDismissListener) {
        mInternalListener.removeDismissListener(listener)
    }

    /**
     * Add a listener to receive a callback when the popup is opened or dismissed.
     *
     * @param listener Listener that will be notified when the popup is opened or dismissed.
     */
    fun addOnPopupOpenListener(listener: PopupOpenListener) {
        mInternalListener.addOpenListener(listener)
    }

    /**
     * Remove a listener from the list of this `[Spinner]`'s listeners
     *
     * @param listener Listener that will be removed from the list.
     */
    fun removeOnPopupOpenListener(listener: PopupOpenListener) {
        mInternalListener.removeOpenListener(listener)
    }

    override fun performClick(): Boolean {
        if (mIsPopupShown) return false

        var handled = true

        try {
            handled = super.performClick()

            // reflecting Spinner.mPopup field
            if (isFieldSpinnerPopupNull) {
                mFieldSpinnerPopup = this.javaClass.superclass?.getDeclaredField(M_POPUP)
            }

            mFieldSpinnerPopup?.let { fieldSpinnerPopup ->
                // enable access checks to Spinner.mPopup
                fieldSpinnerPopup.isAccessible = true

                // get value of mPopup field
                val spinnerPopup = fieldSpinnerPopup.get(this)

                // reflecting SpinnerPopup.isShowing()
                val methodIsShowing = fieldSpinnerPopup.type.getDeclaredMethod(IS_SHOWING)

                // calling Spinner.mPopup.isShowing()
                val isShowing = methodIsShowing.invoke(spinnerPopup) as Boolean

                if (isShowing) {
                    mIsPopupShown = true

                    // make listener handle onShow event
                    mInternalListener.onShow()

                    // replace the default listener with the customized one
                    mListenerSubstitutorFactory.getListenerSubstitutor(spinnerMode).substitute(spinnerPopup)
                }

                // disable access checks to Spinner.mPopup
                fieldSpinnerPopup.isAccessible = false
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return handled
    }

    /**
     * Returns a constant describing how the user selects choices from the spinner.
     *
     * @return the choosing mode of this `[Spinner]`
     */
    val spinnerMode: Int
        get() {
            var result = MODE_UNKNOWN

            try {
                // reflecting Spinner.mPopup field
                if (isFieldSpinnerPopupNull) {
                    mFieldSpinnerPopup = this.javaClass.superclass?.getDeclaredField(M_POPUP)
                }

                mFieldSpinnerPopup?.let { fieldSpinnerPopup ->
                    // get Spinner.DropdownPopup class name
                    fieldSpinnerPopup.isAccessible = true
                    val spinnerPopupClassName = fieldSpinnerPopup.get(this)?.javaClass?.simpleName
                    fieldSpinnerPopup.isAccessible = false

                    when (spinnerPopupClassName) {
                        DIALOG_POPUP -> result = MODE_DIALOG
                        DROPDOWN_POPUP -> result = MODE_DROPDOWN
                        else -> {
                        }
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }

    val isFieldSpinnerPopupNull: Boolean
        get() = mFieldSpinnerPopup == null
}
