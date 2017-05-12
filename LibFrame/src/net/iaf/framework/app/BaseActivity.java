package net.iaf.framework.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {

    /**
     * 处理应用退出
     */
    private ExitHandler exitHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
