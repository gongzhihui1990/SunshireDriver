package net.iaf.framework.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ViewUtil {
    public static void clearInput(ViewGroup group) {
        if (group.getChildCount() >= 1) {
            int size = group.getChildCount();
            for (int i = 0; i < size; i++) {
                View child = group.getChildAt(i);
                if (child instanceof ViewGroup) {
                    clearInput((ViewGroup) child);
                } else {
                    clearInputView(child);
                }
            }
        }
    }

    private static void clearInputView(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setText("");
        }
    }
}
