package com.duchuyctlk.popupdismisscatchablespinner.widget;

import java.lang.reflect.Field;
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

        public void setListener(PopupDismissListener listener) {
            mListener = listener;
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
        }

        public void onShow() {
            if (mListener != null) {
                mListener.onShow();
            }
        }
    }

    private Field mSpinnerPopup = null;

    private InternalListener mInternalListener = new InternalListener();

    public PopupDismissCatchableSpinner(Context context) {
        super(context);
    }

    public PopupDismissCatchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupDismissCatchableSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnPopupDismissListener(PopupDismissListener listener) {
        mInternalListener.setListener(listener);

        try {
            // reflecting Spinner.mPopup field
            if (mSpinnerPopup == null) {
                mSpinnerPopup = this.getClass().getSuperclass().getDeclaredField(Constant.M_POPUP);
            }

            if (mSpinnerPopup == null) {
                return;
            }

            // disable access checks to Spinner.mPopup
            mSpinnerPopup.setAccessible(true);

            // get Spinner.DropdownPopup class name
            String mSpinnerPopupClassName = mSpinnerPopup.get(this).getClass().getSimpleName();

            // check if mSpinnerPopup is a drop down popup
            if (Constant.DROPDOWN_POPUP.equals(mSpinnerPopupClassName)) {
                ((ListPopupWindow) mSpinnerPopup.get(this)).setOnDismissListener(mInternalListener);
            }

            // disable access checks to Spinner.mPopup
            mSpinnerPopup.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean performClick() {
        boolean result = super.performClick();

        try {
            // reflecting Spinner.mPopup field
            if (mSpinnerPopup == null) {
                mSpinnerPopup = this.getClass().getSuperclass().getDeclaredField(Constant.M_POPUP);
            }

            if (mSpinnerPopup == null) {
                return result;
            }

            // disable access checks to Spinner.mPopup
            mSpinnerPopup.setAccessible(true);

            // get Spinner.DropdownPopup class name
            String mSpinnerPopupClassName = mSpinnerPopup.get(this).getClass().getSimpleName();

            // reflecting SpinnerPopup.isShowing()
            Method mIsShowing = mSpinnerPopup.getType()
                    .getDeclaredMethod(Constant.IS_SHOWING, new Class[] {});

            // calling Spinner.mPopup.isShowing()
            boolean mIsShowingResult = (Boolean) mIsShowing.invoke(mSpinnerPopup.get(this), new Object[] {});

            if (mIsShowingResult) {
                // make listener handle onShow event
                mInternalListener.onShow();

                // check if mSpinnerPopup is a dialog popup
                if (Constant.DIALOG_POPUP.equals(mSpinnerPopupClassName)) {
                    // reflecting DialogPopup.mPopup
                    Field mAlertDialog = mSpinnerPopup.get(this).getClass().getDeclaredField(Constant.M_POPUP);

                    // disable access checks to Spinner.mPopup.mPopup
                    mAlertDialog.setAccessible(true);

                    // set onDismissListener
                    ((AlertDialog) mAlertDialog.get(mSpinnerPopup.get(this))).setOnDismissListener(mInternalListener);

                    // enable access checks to Spinner.mPopup.mPopup
                    mAlertDialog.setAccessible(false);
                }
            }

            // enable access checks to Spinner.mPopup
            mSpinnerPopup.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}

