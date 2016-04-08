package com.duchuyctlk.popupdismisscatchablespinner.widget;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Spinner;

public class PopupDismissCatchableSpinner extends Spinner {

    public interface PopupDismissListener {
        void onShow();

        void onDismiss(DialogInterface dialog);
    }

    private class InternalListener implements PopupWindow.OnDismissListener, DialogInterface.OnDismissListener {
        private PopupDismissListener mListener = null;
        private PopupWindow.OnDismissListener mPopupListener = null;

        public void setListener(PopupDismissListener listener) {
            mListener = listener;
        }

        public void setPopupListener(PopupWindow.OnDismissListener listener) {
            mPopupListener = listener;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            if (mListener != null) {
                mListener.onDismiss(dialog);
            }
        }

        @Override
        public void onDismiss() {
            if (mListener != null) {
                mListener.onDismiss(null);
            }
            if (mPopupListener != null) {
                mPopupListener.onDismiss();
            }
        }

        public void onShow() {
            if (mListener != null) {
                mListener.onShow();
            }
        }
    }

    private Field mFieldSpinnerPopup = null;
    private InternalListener mInternalListener = new InternalListener();

    public PopupDismissCatchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnPopupDismissListener(PopupDismissListener listener) {
        mInternalListener.setListener(listener);
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

            // get Spinner.DropdownPopup class name
            String spinnerPopupClassName = spinnerPopup.getClass().getSimpleName();

            // reflecting SpinnerPopup.isShowing()
            Method isShowing = mFieldSpinnerPopup.getType()
                    .getDeclaredMethod(Constant.IS_SHOWING, new Class[]{});

            // calling Spinner.mPopup.isShowing()
            boolean isShowingResult = (boolean) isShowing.invoke(spinnerPopup, new Object[]{});

            if (isShowingResult) {
                // make listener handle onShow event
                mInternalListener.onShow();

                // check if mFieldSpinnerPopup is a dialog popup
                if (Constant.DIALOG_POPUP.equals(spinnerPopupClassName)) {
                    // reflecting DialogPopup.mPopup
                    Field fieldAlertDialog = spinnerPopup.getClass().getDeclaredField(Constant.M_POPUP);

                    // disable access checks to Spinner.mPopup.mPopup
                    fieldAlertDialog.setAccessible(true);

                    // set onDismissListener
                    ((AlertDialog) fieldAlertDialog.get(spinnerPopup)).setOnDismissListener(mInternalListener);

                    // enable access checks to Spinner.mPopup.mPopup
                    fieldAlertDialog.setAccessible(false);
                } else if (Constant.DROPDOWN_POPUP.equals(spinnerPopupClassName)) {
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
}
