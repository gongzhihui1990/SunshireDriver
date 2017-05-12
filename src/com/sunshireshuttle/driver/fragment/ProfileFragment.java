package com.sunshireshuttle.driver.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunshireshuttle.driver.BaseLazyFragment;
import com.sunshireshuttle.driver.MySPEdit;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.SunDriverApplication;
import com.sunshireshuttle.driver.activity.LocationTripRecordsActivity;
import com.sunshireshuttle.driver.activity.PolylineDemoActivity;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.model.BaseResponseBean;
import com.sunshireshuttle.driver.model.DirverToken;

import net.iaf.framework.exception.IException;
import net.iaf.framework.util.Version;

public class ProfileFragment extends BaseLazyFragment {
    android.content.DialogInterface.OnClickListener listenerQuit = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            logoutSuccess();
        }
    };
    android.content.DialogInterface.OnClickListener listenerQuitOffDuty = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            offline();
        }
    };
    private TextView tv_name;
    private TextView tv_id;
    private TextView tv_phone;
    private TextView tv_ctrip;
    private TextView tv_location;
    private TextView tv_version;
    private View ll_loc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onUserVisible() {
        renderState();
        super.onUserVisible();
    }

    private void renderState() {
        DirverToken dirverToken = SunDriverApplication.getInstance().dirverToken;
        tv_ctrip.setText(dirverToken.getTripId());
        tv_location.setText(dirverToken.getState() == 1 ? "Yes" : "No");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        disableBackView();
        ll_loc = view.findViewById(R.id.ll_loc);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_id = (TextView) view.findViewById(R.id.tv_id);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_ctrip = (TextView) view.findViewById(R.id.tv_ctrip);
        tv_location = (TextView) view.findViewById(R.id.tv_location);
        tv_version = (TextView) view.findViewById(R.id.tv_version);
        tv_version.setText("V " + Version.getVersionName());
        View logout = view.findViewById(R.id.logout);
        DirverToken dirverToken = SunDriverApplication.getInstance().dirverToken;
        setTitle("Hi!" + dirverToken.getName());
        tv_name.setText(dirverToken.getName());
        tv_id.setText(dirverToken.getId());
        tv_phone.setText(dirverToken.getPhone());
        tv_ctrip.setText(dirverToken.getTripId());
        tv_location.setText(dirverToken.getState() == 1 ? "Yes" : "No");
        ll_loc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LocationTripRecordsActivity.class));
            }
        });
        ll_loc.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(getActivity(), PolylineDemoActivity.class));

                return false;
            }
        });
        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final DirverToken dirverToken = SunDriverApplication.getInstance().dirverToken;
                switch (dirverToken.getState()) {
                    case 0:
                        new Builder(getActivity()).setTitle("Hint:").
                                setMessage(R.string.quit).setPositiveButton(R.string.ok, listenerQuit).create().show();
                        break;
                    case 1:
                        new Builder(getActivity()).setTitle("Hint:").
                                setMessage(R.string.quit_off_duty).setPositiveButton(R.string.ok, listenerQuitOffDuty).create().show();
                        break;

                    default:
                        break;
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }


    private void offline() {
        SunDriverApplication.getInstance().dirverToken.setState(0);
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<BaseResponseBean> updateViewAsyncCallback = new ProgressCallback<BaseResponseBean>(getBaseActivity()) {
            @Override
            public void onPostExecute(BaseResponseBean arg0) {
                super.onPostExecute(arg0);
                SunDriverApplication.getInstance().dirverToken.setState(0);
                logoutSuccess();
            }

            @Override
            public void onException(IException exception) {
                super.onException(exception);
                SunDriverApplication.getInstance().dirverToken.setState(0);
                logoutSuccess();
            }
        };
        control.updateLocationAction(updateViewAsyncCallback, "9");
    }

    private void logoutSuccess() {
        MySPEdit.getInstance(getContext()).logout();
        getActivity().finish();
    }
}
