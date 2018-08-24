package ru.kazakova_net.bookstore.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class CommonUtils {
    
    /**
     * Reacts to a click outside the EditText widget and hides the keyboard
     *
     * @param ev click event
     * @return the result of the handler assignment
     */
    public static MotionEvent dispatchTouchEvent(MotionEvent ev, View view, InputMethodManager imm) {
        
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (view instanceof EditText) {
                
                view.clearFocus();
                hideKeyboard(view, imm);
            }
        }
        
        return ev;
    }
    
    /**
     * The method hides the keyboard
     */
    private static void hideKeyboard(View view, InputMethodManager imm) {
        if (view != null && imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
