package com.sunshireshuttle.driver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sunshireshuttle.driver.activity.LoginActivity;
import com.sunshireshuttle.driver.activity.MainActivity;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.model.CurrentOrderItem;
import com.sunshireshuttle.driver.notification.NotificationReceiver;
import com.sunshireshuttle.driver.widget.Actions4SimpleDlg;
import com.sunshireshuttle.driver.widget.SimpleDialogFragment;
import com.umeng.analytics.MobclickAgent;

import net.iaf.framework.util.Loger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BaseActivity extends FragmentActivity {

    protected SunDriverApplication application;
    protected DriverCommonControl commonControl = new DriverCommonControl();

    @Override
    protected void onCreate(Bundle arg0) {
        application = SunDriverApplication.getInstance();
        super.onCreate(arg0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        Loger.d(this.getClass().getSimpleName() + " onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        Loger.d(this.getClass().getSimpleName() + " onResume");
    }

    /*
     * name:通知名字，作为通知id使用 content：通知内容 time：倒时时（秒）
     */
    public void addTestLocalNotication(String name, String content, int time, int requestCode) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.SECOND, (int) time);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("content", content);
        PendingIntent pi = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
    }

    public void cancleLocalNotication(int requestCode) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.setClass(this, NotificationReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    @SuppressLint("SimpleDateFormat")
    public void addNotificationsForOrder(CurrentOrderItem orderItem) {
        String etaAlert = orderItem.getEta_alert();
        String cobAlert = orderItem.getCob_alert();
        String title = "";
        String body = "";
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date fireDate = null;
        if (etaAlert != null && !"0000-00-00 00:00".equals(etaAlert)) {
            try {
                Date etaDate = dateFormat1.parse(etaAlert);
                if (etaDate.before(new Date())) {
                    title = "ETA Alert: " + orderItem.getId();
                    body = "Estimated ETA Time: " + etaAlert;
                    addNotification(Integer.valueOf(orderItem.getId()), CurrentOrderItem.AlertEta, title, body,
                            fireDate, orderItem);
                } else {
                    Loger.d("过期");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (cobAlert != null && !"0000-00-00 00:00".equals(cobAlert)) {
            try {
                Date cobDate = dateFormat1.parse(cobAlert);
                if (cobDate.before(new Date())) {
                    title = "COB Alert: " + orderItem.getId();
                    body = "Estimated COB Time: " + cobAlert;
                    addNotification(Integer.valueOf(orderItem.getId()), CurrentOrderItem.AlertCob, title, body,
                            fireDate, orderItem);
                } else {
                    Loger.d("过期");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // Calendar cal = Calendar.getInstance();
        // cal.setTimeInMillis(System.currentTimeMillis());
        // cal.add(Calendar.SECOND, (int) time);
    }

    private void addNotification(int id, int alertType, String title, String body, Date fireDate,
                                 CurrentOrderItem orderItem) {
        int requestCode = alertType + id * 10;
        cancleLocalNotication(requestCode);
        Loger.d("addNotification" + requestCode);
        if (fireDate == null) {
            return;
        }
        // XXX TEST CODE
        // Calendar cal = Calendar.getInstance();
        // cal.setTimeInMillis(System.currentTimeMillis());
        // cal.add(Calendar.SECOND, (int) 2*alertType+8);
        // fireDate=cal.getTime();
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("id", requestCode);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.putExtra("alertType", alertType);
        intent.putExtra(CurrentOrderItem.class.getName(), orderItem);
        PendingIntent pi = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, fireDate.getTime(), pi);
    }

    public SimpleDialogFragment showDialog(String title, String message) {
        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        Bundle args = new Bundle();
        Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
        actions4SimpleDlg.cancelble = false;
        actions4SimpleDlg.title = title;
        actions4SimpleDlg.message = message;
        args.putSerializable("actions", actions4SimpleDlg);
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(actions4SimpleDlg.cancelble);
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        transition.add(dialogFragment, null);
        try {
            transition.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialogFragment;
    }

    public SimpleDialogFragment showDialog(Actions4SimpleDlg actions4SimpleDlg) {
        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("actions", actions4SimpleDlg);
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(actions4SimpleDlg.cancelble);
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        transition.add(dialogFragment, null);
        try {
            transition.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialogFragment;
    }

    @Override
    protected void onDestroy() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop(); // 停止播放视频
                }
                mediaPlayer.release(); // 释放资源
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    // private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (!(this instanceof LoginActivity)) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
            if (surfaceView != null) {
                surfaceView.getHolder().addCallback(new BaseActivity.SurfaceViewPlayerCallback(surfaceView));
            }
        }
    }

    private class SurfaceViewPlayerCallback implements SurfaceHolder.Callback {
        private SurfaceView surfaceView;

        public SurfaceViewPlayerCallback(SurfaceView surfaceView) {
            this.surfaceView = surfaceView;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                playVedio();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void playVedio() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AssetFileDescriptor fd = BaseActivity.this.getAssets().openFd("backgroundplay_final.mp4");
            // AssetFileDescriptor fd =
            // this.getAssets().openFd("backgroundplay_final.mp4");
            mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mediaPlayer.setLooping(true);
            mediaPlayer.setDisplay(surfaceView.getHolder());
            // 通过异步的方式装载媒体资源
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 装载完毕回调
                    mediaPlayer.start();
                }
            });
        }
    }
}
