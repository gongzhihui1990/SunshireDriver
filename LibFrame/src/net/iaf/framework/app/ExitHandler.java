package net.iaf.framework.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 处理应用程序退出
 */
public class ExitHandler {

    public static final String ACTION_EXIT = BaseApplication.getContext().getPackageName() + ".action_exit_app";

    private Activity mActivity = null;

    /**
     * 广播接收器，接收退出的消息，finish掉activity
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (ACTION_EXIT.equals(action)) {
                if (null != mActivity) {
                    mActivity.finish();
                }

            }
        }
    };

    public ExitHandler(Activity activity) {
        mActivity = activity;
    }

    /**
     * 注册广播
     */
    public void register() {
        if (null != mActivity) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_EXIT);
            mActivity.registerReceiver(receiver, filter);
        }
    }

    /**
     * 解除注册广播
     */
    public void unregister() {
        if (null != mActivity) {
            mActivity.unregisterReceiver(receiver);
        }
    }
}
