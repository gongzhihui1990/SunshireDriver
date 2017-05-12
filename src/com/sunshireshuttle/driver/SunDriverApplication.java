package com.sunshireshuttle.driver;

import java.util.List;

import com.sunshireshuttle.driver.model.DirverToken;
import com.sunshireshuttle.driver.service.TripRecordService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import net.iaf.framework.app.BaseApplication;
import net.iaf.framework.util.Loger;

public class SunDriverApplication extends BaseApplication {
    public DirverToken dirverToken;
    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                // 检查Service状态
                checkService();
            }
        }

    };

    private void checkService() {
        ActivityManager manager = (ActivityManager) SunDriverApplication.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        boolean isServiceRunning = false;
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (TripRecordService.class.getClass().equals(service.service.getClassName())) {
                isServiceRunning = true;
            }
        }
        if (!isServiceRunning) {
            Intent i = new Intent(context, TripRecordService.class);
            context.startService(i);
        }
    }

    @Override
    public void onCreate() {
        String processName = getProcessName(this, android.os.Process.myPid());
        Loger.e("onCreate" + processName);
        super.onCreate();
        if (!processName.equals("com.sunshireshuttle.driver")) {
            return;
        }
        Loger.e("onCreate" + processName + "-init");
        dirverToken = MySPEdit.getInstance(context).getDirverToken();
        checkService();
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onTerminate() {
        Loger.e("onTerminate");
        super.onTerminate();
    }

    /**
     * 登陆的用户信息，伴随app生命周期
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static SunDriverApplication getInstance() {
        return (SunDriverApplication) getApplication();
    }

}
