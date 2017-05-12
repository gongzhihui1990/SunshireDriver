package net.iaf.framework.util.android;

import android.os.Build;

import java.lang.reflect.Method;

public class FlymeUtils {

    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

}
