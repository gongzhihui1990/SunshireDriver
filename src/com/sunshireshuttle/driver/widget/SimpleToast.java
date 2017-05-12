package com.sunshireshuttle.driver.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.SunDriverApplication;

public class SimpleToast {
    private static SimpleToast simpleToast;
    private Toast toast;

    private SimpleToast() {
    }

    private static SimpleToast getInatance() {
        if (simpleToast == null) {
            simpleToast = new SimpleToast();
        }
        return simpleToast;
    }

    public static void ToastMessage(int tvStringSrc) {
        ToastMessage(SunDriverApplication.getContext().getString(tvStringSrc));
    }

    public static void ToastMessage(String tvString) {
        getInatance().ToastMessage(SunDriverApplication.getContext(), tvString, 0);
    }

    public static void ToastError(Exception e) {
        getInatance().ToastMessage(SunDriverApplication.getContext(), e.getMessage(), 0);
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param root
     * @param tvString
     */

    @SuppressLint("InflateParams")
    private void ToastMessage(Context context, String tvString, int length) {
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(tvString)) return; // 空信息不显示
        try {
            View layout = LayoutInflater.from(context).inflate(R.layout.layout_simple_toast, null, false);
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(tvString);
            toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    toast.cancel();
                }
            }, 1000);
        } catch (Exception e) {
        }
    }
}
