/**
 * 为了静态方法获取Context，自己实现的Application需要继承该类
 */
package net.iaf.framework.app;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import net.iaf.framework.util.Loger;

import java.util.List;

/**
 * 一个进程对应一个Application实例
 */
public class BaseApplication extends Application {
    public static Context context;
    public static BaseApplication baseApplication;

    public float ScreenWidth;
    public float ScreenHeight;
    public float density;

    private Handler handler;

    private CheckStatusThread thread;

    /**
     * 屏幕是否点亮
     */
    private boolean isScreenOn = true;
    private boolean inFront = false;
    /*
     * 应用在前台时,监听屏幕关闭状态,ACTION_SCREEN_OFF时关闭定位
     * ACTION_SCREEN_ON时开启定位
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                Loger.i("SCREEN_ON");
                isScreenOn = true;
                onScreenOn();
            } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                Loger.i("SCREEN_OFF");
                isScreenOn = false;
                onScreenOff();
            }
        }
    };

    public static Context getContext() {
        return context;
    }

    public static BaseApplication getApplication() {
        return baseApplication;
    }

    private void init() {
        if (context == null) {
            context = this.getApplicationContext();
        }
        if (baseApplication == null) {
            baseApplication = this;
        }
        //主线程中的操作
        if (inMainProcess()) {
            WindowManager wmManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wmManager.getDefaultDisplay().getMetrics(dm);
            ScreenWidth = (float) dm.widthPixels;
            ScreenHeight = (float) dm.heightPixels;
            density = dm.density;

            handler = new AppHandler();
            thread = new CheckStatusThread();
            thread.start();

            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            registerReceiver(receiver, filter);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    /**
     * 切换到前台
     */
    protected void onSwitch2Front() {
    }

    /**
     * 切换到后台
     */
    protected void onSwitch2Back() {
    }

    /**
     * 退出应用时,要做的操作,释放资源
     */
    protected void onExitApp() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
        }
    }

    /**
     * 屏幕点亮
     */
    protected void onScreenOn() {
    }

    /**
     * 屏幕熄灭
     */
    protected void onScreenOff() {
    }

    /**
     * 屏幕是否点亮
     */
    public boolean isScreenOn() {
        return this.isScreenOn;
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        //关闭所有activity
        Intent intent = new Intent(ExitHandler.ACTION_EXIT);
        sendBroadcast(intent);

        onExitApp();
    }

    /**
     * 退出所有activity
     */
    public void exitAllActivities() {
        //关闭所有activity
        Intent intent = new Intent(ExitHandler.ACTION_EXIT);
        sendBroadcast(intent);
    }

    /**
     * 应用是否在前台运行
     */
    public boolean inFront() {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前应用是否有activity仍然在运行
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    protected boolean isTaskRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningTaskInfo task : manager.getRunningTasks(3)) {
            if (getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断程序是否运行在主进程中
     * （主进程名默认与包名相同的进程名,若在AndroidManifest.xml中手动配置了processName,此方法可能不适用）
     */
    public boolean inMainProcess() {
        int myPid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String mainProcessName = getPackageName();
        for (RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == myPid) {
                /**
                 * 主进程名称默认为包名,在manifest中配置
                 * 若在AndroidManifest.xml中手动配置了processName,此方法可能不适用
                 */
                if (mainProcessName.equals(appProcess.processName)) {
                    Loger.i("process id:" + appProcess.pid);
                    Loger.i("MainProcess name:" + appProcess.processName);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断程序是否运行在ProcessName进程中
     */
    public boolean inProcess(String processName) {
        int myPid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == myPid) {
                if (processName.equals(appProcess.processName)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 检查Service是否在运行
     *
     * @param className Service的Class名称
     * @return
     */
    public boolean isServiceRunning(String className) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(300);

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                return true;
            }
        }
        return false;
    }

    public boolean isInFront() {
        return inFront;
    }

    /**
     * 检查应用前后台状态线程
     */
    class CheckStatusThread extends Thread {

        @Override
        public void run() {
            while (true) {
                if (inFront() && isTaskRunning()) {
                    if (!inFront) {
                        Message msg = handler.obtainMessage(0);
                        msg.sendToTarget();
                    }
                    inFront = true;
                } else {
                    if (inFront) {
                        Message msg = handler.obtainMessage(1);
                        msg.sendToTarget();
                    }
                    inFront = false;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

    }

    @SuppressLint("HandlerLeak")
    class AppHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Loger.i("app switch to forground");
                onSwitch2Front();
            } else if (msg.what == 1) {
                Loger.i("app switch to background");
                onSwitch2Back();
            }
        }
    }
}
