package com.sunshireshuttle.driver.control;


import android.app.ProgressDialog;
import android.text.TextUtils;

import com.sunshireshuttle.driver.BaseActivity;

import net.iaf.framework.exception.IException;

public class ProgressCallback<Result> extends BaseViewCallback<Result> {
    //	private FragmentManager manager;
    private ProgressDialog progressDialog;
    private BaseActivity activity;

    public ProgressCallback(BaseActivity activity) {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("connectting...");
    }

    @Override
    public void onException(IException exception) {
        super.onException(exception);
        if (exception != null) {
            String error = exception.getMessage();
            if (TextUtils.isEmpty(error)) {
                error = "exception of-" + exception.getClass().getName();
            }
            activity.showDialog(null, error);
        }
    }

    public void setProgressMessage(String message) {
        progressDialog.setMessage(message);
    }

    @Override
    protected void showProgress() {
        try {
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void dismissProgress() {
        progressDialog.dismiss();
    }
}
