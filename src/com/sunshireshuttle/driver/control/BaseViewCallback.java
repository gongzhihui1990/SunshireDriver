package com.sunshireshuttle.driver.control;

import net.iaf.framework.controller.BaseController.UpdateViewAsyncCallback;
import net.iaf.framework.exception.IException;

/**
 * @param <Result>
 * @author Heisenberg
 */
public abstract class BaseViewCallback<Result> implements
        UpdateViewAsyncCallback<Result> {

    @Override
    public void onPreExecute() {
        showProgress();
    }

    @Override
    public void onException(IException exception) {
        dismissProgress();
        if (exception != null) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onPostExecute(Result arg0) {
        dismissProgress();
    }

    @Override
    public void onCancelled() {
        dismissProgress();
    }

    protected abstract void showProgress();

    protected abstract void dismissProgress();

}
