package net.iaf.framework.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class BaseFragmentActivity extends FragmentActivity {

    /**
     * 处理应用退出
     */
    private ExitHandler exitHandler = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        exitHandler = new ExitHandler(this);
        exitHandler.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exitHandler.unregister();
    }

    /**
     * 显示提示消息
     *
     * @param text
     */
    protected void showMsgToast(String text) {
        Toast.makeText(BaseApplication.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 退出应用
     */
    protected void exitApp() {
        BaseApplication.getApplication().exitApp();
    }
}
