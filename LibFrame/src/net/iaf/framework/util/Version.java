package net.iaf.framework.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

import net.iaf.framework.app.BaseApplication;

/**
 * 应用版本相关的工具类
 *
 * @author: jiangsy
 * @version: 1.0
 */
public class Version {

    /**
     * 获取当前版本号
     *
     * @return
     */
    public static String getVersionName() {
        Context context = BaseApplication.getContext();
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            Loger.d(packInfo.versionName);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前App的versionCode（从1开始），如果没找到（NameNotFoundException），返回0
     */
    public static int getVersionCode() {
        Context context = BaseApplication.getContext();
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            Loger.d("versionCode = " + packInfo.versionCode);
            return packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取设备id（IMEI号）
     */
    public static String getDeviceID() {
        Context context = BaseApplication.getContext();
        return ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }
}
