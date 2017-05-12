package com.sunshireshuttle.driver.adapter;

import android.widget.BaseAdapter;

public abstract class MyBaseAdapter extends BaseAdapter {
    @Override
    public long getItemId(int position) {
        return 0;
    }
}
