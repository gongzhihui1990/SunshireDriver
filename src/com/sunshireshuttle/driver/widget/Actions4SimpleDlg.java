package com.sunshireshuttle.driver.widget;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.Serializable;

public class Actions4SimpleDlg implements Serializable, Parcelable {
    private static final long serialVersionUID = 3889542938169910637L;
    public transient boolean cancelble = false;
    public transient boolean showCancel = false;
    public transient View layoutView = null;
    public transient String title = null;
    public transient String message = null;
    public transient int textColorRes = android.R.color.black;
    public transient OnClickListener confirmListener;
    public transient OnClickListener cancleListener;
    public transient OnClickListener otherListener;

    public transient String btn1;
    public transient String btn2;
    public transient String btn3;

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

    }
}