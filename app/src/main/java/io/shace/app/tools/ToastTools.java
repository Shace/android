package io.shace.app.tools;

import android.content.Context;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import io.shace.app.App;

/**
 * Created by melvin on 4/30/14.
 */
public class ToastTools {
    static ToastTools sInstance = null;
    static protected Context sContext = App.getContext();

    Toast lastToast = null;

    private ToastTools() {
    }

    static public ToastTools getInstance() {
        if (sInstance == null) {
            sInstance = new ToastTools();
        }

        return sInstance;
    }

    /**
     * Alias for getInstance
     */
    static public ToastTools use() {
        return getInstance();
    }


    /**
     * Display a long toast. The previous toast will be cancelled
     */
    public void longToast(String message) {
        if (lastToast != null) {
            lastToast.cancel();
        }

        lastToast = Toast.makeText(sContext, message, Toast.LENGTH_LONG);

        InputMethodManager imm = (InputMethodManager) sContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            lastToast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
        }

        lastToast.show();
    }

    /**
     * Display a long toast. The previous toast will be cancelled
     */
    public void longToast(int stringID) {
        String message = sContext.getString(stringID);
        longToast(message);
    }
}
