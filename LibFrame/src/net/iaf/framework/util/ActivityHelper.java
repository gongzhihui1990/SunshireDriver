/**
 * @Description: TODO
 * @author: {Zhou Haitao.  }
 * @version: 1.0
 * @see
 */

package net.iaf.framework.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;


/**
 * Activity帮助类
 *
 * @author Bob
 */
public class ActivityHelper {
    /**
     * 获取屏幕宽
     *
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        WindowManager wmManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wmManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        WindowManager wmManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wmManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static int screenWidth(Context mContext) {
        int width = 0;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth();  // deprecated
        }
        return width;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static int screenHeight(Context mContext) {
        int height = 0;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height = display.getHeight();  // deprecated
        }
        return height;
    }

    /**
     * px 转为 dp
     */
    public static int px2dip(float pxValue, Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (pxValue / dm.density + 0.5f);
    }

    /**
     * dp转为px
     */
    public static int dp2px(float dpValue, Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (dpValue * dm.density + 0.5f);
    }

    /**
     * 用于在onCreate中获取view的实际高度
     *
     * @param view            view实例
     * @param horizontalSpace view两侧与屏幕的间隔(单位dip)
     * @return view的实际高度
     */
    public static int getViewHeight(Context context, View view, int horizontalSpace) {
        if (null == view) {
            return 0;
        }

        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        int childWidthSpec = MeasureSpec.makeMeasureSpec(
                (int) (dm.widthPixels - (horizontalSpace * dm.density)),
                MeasureSpec.EXACTLY);

        view.measure(childWidthSpec, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    /**
     * 用于在onCreate中获取view的状态栏的高度
     *
     * @param Activity activity实例
     * @return statusBarHeight的实际高度
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }


}
