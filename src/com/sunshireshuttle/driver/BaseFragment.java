package com.sunshireshuttle.driver;

import com.sunshireshuttle.driver.utils.DateUtils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

public class BaseFragment extends Fragment {

    private BaseActivity baseActivity;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        baseActivity = (BaseActivity) activity;
        super.onAttach(activity);
    }

    protected View findViewById(int id) {
        return getView().findViewById(id);
    }

    ;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }

    protected void setTitle(CharSequence text) {
        TextView tv_name = (TextView) getView().findViewById(R.id.tv_toolbar_title);
        if (tv_name != null) {
            tv_name.setText(text);
        }
    }

    protected void disableBackView() {
        View back = getView().findViewById(R.id.ll_back);
        if (back != null) {
            back.setVisibility(View.GONE);
        }
    }
}
