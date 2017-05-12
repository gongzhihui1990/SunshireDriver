/**
 * @author: Zhou Haitao
 */

package net.iaf.framework.imgload;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import net.iaf.framework.imgload.ImageWorker.AsyncIndicator;

public abstract class LoadingIndicatorView extends LinearLayout {

    //-------------------PACKAGE VISIBLE-------------------------------
    private AsyncIndicator asyncIndicator;

    public LoadingIndicatorView(Context context) {
        super(context);
    }

    public LoadingIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param progress 0-100  100表示加载完成  0开始加载, -1表示加载失败
     */
    public abstract void onProgressUpdate(int progress);

    AsyncIndicator getAsyncIndicator() {
        return asyncIndicator;
    }

    public void setAsyncIndicator(AsyncIndicator asyncIndicator) {
        this.asyncIndicator = asyncIndicator;
    }

}
