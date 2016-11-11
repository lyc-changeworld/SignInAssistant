package com.example.achuan.bombtest.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by achuan on 16-11-11.
 * 功能：
 */
public class SnackbarUtil {

    public static void showLong(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void showShort(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

}
