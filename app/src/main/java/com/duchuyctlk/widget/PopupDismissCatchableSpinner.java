package com.duchuyctlk.widget;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.duchuyctlk.Constant;

public class PopupDismissCatchableSpinner extends Spinner {

    private static final int MODE_UNKNOWN = -1;

    /**
     * Interface used to allow the creator of a <code>{@link Spinner}</code>
     * to run some code when the Spinner's Popup is opened or dismissed.
     */
    public interface PopupDismissListener {
        void onShow();

        void onDismiss(DialogInterface dialog);
    }

    private class InternalListener implements PopupWindow.OnDismissListener, DialogInterface.OnDismissListener {
        private List<PopupDismissListener> mListeners = new ArrayList<>();
        private PopupWindow.OnDismissListener mPopupListener = null;

        public void addListener(PopupDismissListener listener) {
            if (listener != null && !mListeners.contains(listener)) {
                mListeners.add(listener);
            }
        }

        public void removeListener(PopupDismissListener listener) {
            if (listener != null) {
                mListeners.remove(listener);
            }
        }

        protected void setPopupListener(PopupWindow.OnDismissListener listener) {
            mPopupListener = listener;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            for (PopupDismissListener listener : mListeners) {
                listener.onDismiss(dialog);
            }
        }

        @Override
        public void onDismiss() {
            for (PopupDismissListener listener : mListeners) {
                listener.onDismiss(null);
            }

            if (mPopupListener != null) {
                mPopupListener.onDismiss();
            }
        }

        public void onShow() {
            for (PopupDismissListener listener : mListeners) {
                listener.onShow();
            }
        }
    }

    private Field mFieldSpinnerPopup = null;
    private InternalListener mInternalListener = new InternalListener();

    /**
     * Construct a new spinner with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public PopupDismissCatchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Add a listener to receive a callback when the popup is opened or dismissed.
     *
     * @param listener Listener that will be notified when the popup is opened or dismissed.
     */
    public void addOnPopupDismissListener(PopupDismissListener listener) {
        mInternalListener.addListener(listener);
    }

    /**
     * Remove a listener from the list of this <code>{@link Spinner}</code>'s listeners
     *
     * @param listener Listener that will be removed from the list.
     */
    public void removeOnPopupDismissListener(PopupDismissListener listener) {
        mInternalListener.removeListener(listener);
    }

    @Override
    public boolean performClick() {
        boolean result = super.performClick();

        try {
            // reflecting Spinner.mPopup field
            if (mFieldSpinnerPopup == null) {
                mFieldSpinnerPopup = this.getClass().getSuperclass().getDeclaredField(Constant.M_POPUP);
            }

            // disable access checks to Spinner.mPopup
            mFieldSpinnerPopup.setAccessible(true);

            // get value of mPopup field
            Object spinnerPopup = mFieldSpinnerPopup.get(this);

            // reflecting SpinnerPopup.isShowing()
            Method isShowing = mFieldSpinnerPopup.getType()
                    .getDeclaredMethod(Constant.IS_SHOWING, (Class[]) null);

            // calling Spinner.mPopup.isShowing()
            boolean isShowingResult = (boolean) isShowing.invoke(spinnerPopup, (Object[]) null);

            if (isShowingResult) {
                // make listener handle onShow event
                mInternalListener.onShow();

                // check if mFieldSpinnerPopup is a dialog popup
                if (getSpinnerMode() == MODE_DIALOG) {
                    // reflecting DialogPopup.mPopup
                    Field fieldAlertDialog = spinnerPopup.getClass().getDeclaredField(Constant.M_POPUP);

                    // disable access checks to Spinner.mPopup.mPopup
                    fieldAlertDialog.setAccessible(true);

                    // set onDismissListener
                    ((AlertDialog) fieldAlertDialog.get(spinnerPopup)).setOnDismissListener(mInternalListener);

                    // enable access checks to Spinner.mPopup.mPopup
                    fieldAlertDialog.setAccessible(false);
                } else if (getSpinnerMode() == MODE_DROPDOWN) {
                    // reflecting Spinner.mPopup.mPopup
                    Field fieldPopupWindow = ListPopupWindow.class.getDeclaredField(Constant.M_POPUP);
                    fieldPopupWindow.setAccessible(true);

                    // reflecting Spinner.mPopup.mPopup.OnDismissListener
                    Field fieldOnDismissListener = PopupWindow.class.getDeclaredField(Constant.ON_DISMISS_LISTENER);
                    fieldOnDismissListener.setAccessible(true);

                    // get the default listener and set into the custom listener
                    PopupWindow.OnDismissListener listener = (PopupWindow.OnDismissListener)
                            fieldOnDismissListener.get(fieldPopupWindow.get(spinnerPopup));
                    mInternalListener.setPopupListener(listener);

                    // replace the default listener by the custom one
                    ((ListPopupWindow) spinnerPopup).setOnDismissListener(mInternalListener);

                    fieldOnDismissListener.setAccessible(false);
                    fieldPopupWindow.setAccessible(false);
                }
            }

            // enable access checks to Spinner.mPopup
            mFieldSpinnerPopup.setAccessible(false);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    /**
     * Returns a constant describing how the user selects choices from the spinner.
     *
     * @return the choosing mode of this <code>{@link Spinner}</code>
     */
    public int getSpinnerMode() {
        int result = MODE_UNKNOWN;

        try {
            // reflecting Spinner.mPopup field
            if (mFieldSpinnerPopup == null) {
                mFieldSpinnerPopup = this.getClass().getSuperclass().getDeclaredField(Constant.M_POPUP);
            }

            // get Spinner.DropdownPopup class name
            mFieldSpinnerPopup.setAccessible(true);
            String spinnerPopupClassName = mFieldSpinnerPopup.get(this).getClass().getSimpleName();
            mFieldSpinnerPopup.setAccessible(false);

            switch (spinnerPopupClassName) {
                case Constant.DIALOG_POPUP:
                    result = MODE_DIALOG;
                    break;
                case Constant.DROPDOWN_POPUP:
                    result = MODE_DROPDOWN;
                    break;
                default:
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return result;
    }
}
