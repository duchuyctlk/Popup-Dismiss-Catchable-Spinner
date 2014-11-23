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
	
	public interface PopupDismissListener extends PopupWindow.OnDismissListener, DialogInterface.OnDismissListener {
		public void onShow();
	}
	
	private Field mSpinnerPopup = null;
	
	private PopupDismissListener mPopupDismissListener = null;
	
    public PopupDismissCatchableSpinner(Context context) {
        super(context);
    }

    public PopupDismissCatchableSpinner(Context context, int mode) {
        super(context, mode);
    }

    public PopupDismissCatchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupDismissCatchableSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PopupDismissCatchableSpinner(Context context, AttributeSet attrs, int defStyle, int mode) {
        super(context, attrs, defStyle, mode);
    }
    
    public void setOnPopupDismissListener(PopupDismissListener listener) {
    	mPopupDismissListener = listener;
    	
    	try {
    		// reflecting Spinner.mPopup field
    		if (mSpinnerPopup == null) {
    			mSpinnerPopup = this.getClass().getSuperclass().getDeclaredField("mPopup");
    		}
    		
    		if (mSpinnerPopup == null) {
    			return;
    		}

    		// disable access checks to Spinner.mPopup 
			mSpinnerPopup.setAccessible(true);
			
			// get Spinner.DropdownPopup class name
			String mSpinnerPopupClassName = mSpinnerPopup.get(this).getClass().getSimpleName();
						
    		// check if mSpinnerPopup is a drop down popup 
    		if ("DropdownPopup".equals(mSpinnerPopupClassName)) {
    			((ListPopupWindow) mSpinnerPopup.get(this)).setOnDismissListener(mPopupDismissListener);
    		} else {    			
    			// we set onDismissListener to dialog popup in performClick
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
    			mSpinnerPopup = this.getClass().getSuperclass().getDeclaredField("mPopup");
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
					.getDeclaredMethod("isShowing", new Class[] {});
			
			// calling Spinner.mPopup.isShowing()
			boolean mIsShowingResult = (Boolean) mIsShowing.invoke(mSpinnerPopup.get(this), new Object[] {});
			
			if (mIsShowingResult) {
				// make listener handle onShow event
				if (mPopupDismissListener != null) {
					mPopupDismissListener.onShow();
				}
				
				// check if mSpinnerPopup is a dialog popup
				if ("DialogPopup".equals(mSpinnerPopupClassName)) {
					// reflecting DialogPopup.mPopup
					Field mAlertDialog = mSpinnerPopup.get(this).getClass().getDeclaredField("mPopup");
					
					// disable access checks to Spinner.mPopup.mPopup
					mAlertDialog.setAccessible(true);
					
					// set onDismissListener
					((AlertDialog) mAlertDialog.get(mSpinnerPopup.get(this))).setOnDismissListener(mPopupDismissListener);
					
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

