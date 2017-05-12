package com.sunshireshuttle.driver.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.iaf.framework.util.Loger;

import com.sunshireshuttle.driver.BaseFragment;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.utils.DateUtils;

public class DisableFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_disable, container, false);
    }
}
