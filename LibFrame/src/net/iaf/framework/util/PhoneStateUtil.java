package net.iaf.framework.util;

import android.content.ContentResolver;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import net.iaf.framework.app.BaseApplication;

import java.lang.reflect.Method;

/**
 * 获取电话状态的工具类
 *
 * @author Bob
 */
public class PhoneStateUtil {

    /**
     * 飞行模式是否开启
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean isAirplaneModeOn(Context context) {
        return (Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0);
    }

    /**
     * 网络连接状态判断
     *
     * @return
     */
    public static boolean hasInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否是使用移动网络连接（GPRS，3G etc）
     */
    public static boolean isUsingMobileConnect(Context context) {
        System.err.println("context" + context);
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null
                && info.getType() == ConnectivityManager.TYPE_MOBILE
                && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 存储卡是否处于可写状态
     *
     * @return true表示可写，false表示不可写
     * @Title: exterStorageReady
     */
    public static boolean extStorageReady() {
        // 获取SdCard状态
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                && Environment.getExternalStorageDirectory().canWrite()) {
            return true;

        }
        return false;
    }

    /**
     * 计算sd卡剩余空间，返回单位K
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static double getSDCardStateAndAvailableSize() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_CHECKING.equals(state)) {
        }
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return 0;
        }
        String direction = Environment.getExternalStorageDirectory().toString();
        try {
            StatFs stat = new StatFs(direction);
            return stat.getAvailableBlocks() * (long) stat.getBlockSize()
                    / 1024f;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * GPS是否处于可写状态
     */
    public static boolean isGPSOpened(Context context) {
        /*
         * //2.1下有问题，2.2以上正常 LocationManager alm = (LocationManager) context
		 * .getSystemService(Context.LOCATION_SERVICE); if (alm
		 * .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
		 * return true; } else { return false; }
		 */

        try {
            Class<?> secureClass = ClassLoader.getSystemClassLoader().loadClass(
                    "android.provider.Settings$Secure");
            Method isMethod = secureClass.getMethod(
                    "isLocationProviderEnabled", ContentResolver.class,
                    String.class);
            Boolean ret = (Boolean) isMethod.invoke(secureClass,
                    context.getContentResolver(), LocationManager.GPS_PROVIDER);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * SD卡是否可用的判断
     *
     * @return
     */
    public static String getExternalStoragePath() {
        // 获取SdCard状态
        String state = android.os.Environment.getExternalStorageState();
        // 判断SdCard是否存在并且是可用的
        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
            if (android.os.Environment.getExternalStorageDirectory().canWrite()) {

                return android.os.Environment.getExternalStorageDirectory()
                        .getPath();
            }
        }
        return null;
    }

    /**
     * 获取当前设备唯一ID
     */
    public static String getDeviceUniqueId() {
        TelephonyManager tm = (TelephonyManager) BaseApplication.context.getSystemService(Context.TELEPHONY_SERVICE);
        String UniqueId = tm.getDeviceId();
        return UniqueId;
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: getIMSI
     * @Description: 获取手机IMSI信息
     */
    public static String getIMSI() {
        TelephonyManager tel = (TelephonyManager) BaseApplication.context.getSystemService(Context.TELEPHONY_SERVICE);
        String simOperator = tel.getSimOperator();
        simOperator = (simOperator == null ? "" : simOperator);
        return simOperator;
    }

    /**
     * @param @return
     * @return boolean
     * @throws
     * @Title: chinaSimCard
     * @Description: Sim卡是否是中国的
     */
    public static boolean chinaSimCard() {
        return getIMSI().startsWith("460");
    }
}
