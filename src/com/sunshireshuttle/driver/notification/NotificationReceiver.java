package com.sunshireshuttle.driver.notification;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.activity.MainActivity;

import net.iaf.framework.util.Loger;

import java.util.List;

public class NotificationReceiver extends BroadcastReceiver {
    public NotificationReceiver() {
    }

    protected static boolean isTopActivity(Context activity) {
        String packageName = activity.getPackageName();
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            Log.d("test", "---------------包名-----------" + tasksInfo.get(0).topActivity.getPackageName());
            // 应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context, intent);
        return;
    }

    @SuppressLint("InlinedApi")
    public void showNotification(Context context, Intent intent) {
        NotificationManager barmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent appIntent = new Intent(context, MainActivity.class);
        appIntent.putExtras(intent.getExtras());
        appIntent.putExtra("page_position", 0);
        int notificationId = intent.getIntExtra("id", 0);
        Loger.d("notificationId" + notificationId);
        PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId, appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        String app_name = context.getResources().getString(R.string.app_name);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(app_name + ":" + intent.getStringExtra("title"))
                .setContentText(intent.getStringExtra("body"))
                // <span style="font-family: Arial;">/设置通知栏显示内容</span>
                .setContentIntent(contentIntent) // 设置通知栏点击意图
                // .setNumber(number) //设置通知集合的数量
                .setTicker(intent.getStringExtra("body")) // 通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_HIGH) // 设置该通知优先级
                .setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notifiy_logo))
                .setDefaults(Notification.DEFAULT_ALL)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音
                // // requires VIBRATE permission
                .setSmallIcon(R.drawable.notifiy_logo);// 设置通知小ICON
        Notification notification = mBuilder.build();
        barmanager.notify(notificationId, notification);
    }

}