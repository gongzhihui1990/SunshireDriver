package com.sunshireshuttle.driver.fragment;

import java.util.Date;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.sunshireshuttle.driver.BaseLazyFragment;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.SunDriverApplication;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.model.BaseResponseBean;
import com.sunshireshuttle.driver.model.DirverToken;
import com.sunshireshuttle.driver.model.TripPrivateQueryResponse;
import com.sunshireshuttle.driver.service.TripRecordService;
import com.sunshireshuttle.driver.widget.Actions4SimpleDlg;
import com.sunshireshuttle.driver.widget.SimpleDialogFragment;
import com.sunshireshuttle.driver.widget.SimpleToast;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.iaf.framework.util.Loger;
import net.iaf.framework.util.TimeHelper;

public class LocationFragment extends BaseLazyFragment implements OnMapReadyCallback {

    private Button btn_duty;
    private TextView tv_duty_state;
    private TextView tv_test;
    private GoogleMap mMap;

    private GoogleMap getMap() {
        return mMap;
    }

    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(Location.class.getName());
            String time = TimeHelper.dateToStr(new Date(location.getTime()), "MM:dd HH:mm:SS");
            CharSequence text = location.getLatitude() + "φ-" + location.getLongitude() + "λ" + "\n" + time;
            tv_test.setText(text);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    private void setUpMap() {
        SupportMapFragment map = new SupportMapFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.map, map);
        transaction.commit();
        map.getMapAsync(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        View cus_toolbar = getView().findViewById(R.id.cus_toolbar);
        cus_toolbar.setVisibility(View.GONE);
        tv_test = (TextView) getView().findViewById(R.id.tv_test);
        tv_duty_state = (TextView) getView().findViewById(R.id.tv_duty_state);
        btn_duty = (Button) getView().findViewById(R.id.btn_duty);
        btn_duty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int State = SunDriverApplication.getInstance().dirverToken.getState();
                switch (State) {
                    case 0:
                        online();
                        break;
                    case 1:
                        offline();
                        break;
                }
            }
        });
        updateDirverStateUI();
        setUpMap();
        enableAction(false);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (getMap() != null) {
            getMap().setMyLocationEnabled(false);
        }
        super.onDestroyView();
    }

    private void updateDirverStateUI() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                DirverToken dirverToken = SunDriverApplication.getInstance().dirverToken;
                switch (dirverToken.getState()) {
                    case 0:
                        btn_duty.setBackgroundResource(R.drawable.btn_duty_xml1);
                        tv_duty_state.setText("offline");
                        btn_duty.setText("GET STARTED");
                        // tv_test.setVisibility(View.GONE);
                        if (getMap() != null) {
                            getMap().setMyLocationEnabled(false);
                        } else {
                            locationEnabled = false;
                        }
                        break;
                    case 1:
                        btn_duty.setBackgroundResource(R.drawable.btn_duty_xml2);
                        tv_duty_state.setText("online");
                        btn_duty.setText("TAKE OFFLINE");
                        // tv_test.setVisibility(View.VISIBLE);
                        if (getMap() != null) {
                            getMap().setMyLocationEnabled(true);
                        } else {
                            locationEnabled = true;
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void online() {
        if (!TripRecordService.onlocationPerperd) {
            SimpleToast.ToastMessage("Wait gps working,try later");
            return;
        }
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<BaseResponseBean> updateViewAsyncCallback = new ProgressCallback<BaseResponseBean>(
                getBaseActivity()) {
            @Override
            public void onPostExecute(BaseResponseBean arg0) {
                super.onPostExecute(arg0);
                onLineSuccessed();
            }
        };
        control.updateLocationAction(updateViewAsyncCallback, "1");

    }

    private void onLineSuccessed() {
        // 设置加油站可见
        SunDriverApplication.getInstance().dirverToken.setState(1);
        updateDirverStateUI();
        enableAction(true);
        queryPrivate();
    }

    private void updateGasPoint(String price) {
        new DriverCommonControl().updateGasPoint(new ProgressCallback<BaseResponseBean>(getBaseActivity()) {
            @Override
            public void onPostExecute(BaseResponseBean arg0) {
                super.onPostExecute(arg0);
                if (arg0.isResponseOK()) {
                    getBaseActivity().showDialog(getString(R.string.hint), "Add Gas Point Successed");
                }
            }

        }, price);
    }

    private void queryPrivate() {
        new DriverCommonControl().queryPrivate(new ProgressCallback<TripPrivateQueryResponse>(getBaseActivity()) {

            @Override
            public void onPostExecute(TripPrivateQueryResponse arg0) {
                super.onPostExecute(arg0);
                if (arg0.getIs_private()) {
                    privateState = 1;
                } else {
                    privateState = 0;
                }
                enableAction(true);
            }
        });
    }

    private int privateState = -1;

    private int getPrivateState() {
        return privateState;
    }

    // TODO
    private void enableAction(boolean enable) {
        TextView tv_action = (TextView) getView().findViewById(R.id.tv_action);
        ImageView set_mode_icon = (ImageView) findViewById(R.id.set_mode_icon);
        ImageView add_gas = (ImageView) findViewById(R.id.add_gas);

        if (enable) {
            tv_action.setTextColor(getResources().getColor(R.color.blue));
            switch (getPrivateState()) {
                case 0://work mode
                    set_mode_icon.setImageResource(R.drawable.icon_launcher);
                    tv_action.setText("Set Private");
                    tv_action.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO set Private 6
                            new DriverCommonControl().updateSpecificLocationWithAction(
                                    new ProgressCallback<BaseResponseBean>(getBaseActivity()) {
                                        @Override
                                        public void onPostExecute(BaseResponseBean arg0) {
                                            super.onPostExecute(arg0);
                                            queryPrivate();
                                        }
                                    }, "6");
                        }
                    });
                    break;
                case 1:// private mode
                    set_mode_icon.setImageResource(R.drawable.icon_private);
                    tv_action.setText("Set Work");
                    tv_action.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO set Work 7
                            new DriverCommonControl().updateSpecificLocationWithAction(
                                    new ProgressCallback<BaseResponseBean>(getBaseActivity()) {
                                        @Override
                                        public void onPostExecute(BaseResponseBean arg0) {
                                            super.onPostExecute(arg0);
                                            queryPrivate();
                                        }
                                    }, "7");
                        }
                    });
                    break;
                default:
                    set_mode_icon.setImageResource(R.drawable.icon_unknow);
                    tv_action.setText("Query");
                    tv_action.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            queryPrivate();
                        }
                    });
            }
            add_gas.setVisibility(View.VISIBLE);
            add_gas.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                    actions4SimpleDlg.cancelble = true;
                    View view = getBaseActivity().getLayoutInflater().inflate(R.layout.layout_add_gas_dialog, null, false);
                    actions4SimpleDlg.layoutView = view;
                    final SimpleDialogFragment dialog = getBaseActivity().showDialog(actions4SimpleDlg);
                    final EditText ed_price = (EditText) view.findViewById(R.id.ed_price);
                    ed_price.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String text = s.toString();
                            if (text.contains(".")) {
                                int index = text.indexOf(".");
                                if (index + 3 < text.length()) {
                                    text = text.substring(0, index + 3);
                                    ed_price.setText(text);
                                    ed_price.setSelection(text.length());
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    view.findViewById(R.id.btn_2).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            final String price = ed_price.getText().toString();
                            if (TextUtils.isEmpty(price)) {
                                return;
                            }
                            try {
                                Double priceLong = Double.valueOf(price);
                                Loger.d("priceLong-" + priceLong);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                            updateGasPoint(price);
                        }
                    });
                    view.findViewById(R.id.btn_1).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                        }
                    });
                }
            });

        } else {
            add_gas.setVisibility(View.INVISIBLE);
            tv_action.setOnClickListener(null);
            set_mode_icon.setImageResource(R.drawable.icon_unknow);
            tv_action.setText("Unknow");
            tv_action.setTextColor(getResources().getColor(R.color.gray));
        }
    }

    private boolean locationEnabled = false;

    private void offline() {
        SunDriverApplication.getInstance().dirverToken.setState(0);
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<BaseResponseBean> updateViewAsyncCallback = new ProgressCallback<BaseResponseBean>(
                getBaseActivity()) {
            @Override
            public void onPostExecute(BaseResponseBean arg0) {
                super.onPostExecute(arg0);
                SunDriverApplication.getInstance().dirverToken.setState(0);
                updateDirverStateUI();
            }
        };
        control.updateLocationAction(updateViewAsyncCallback, "9");
        // 设置加油站可见不可见
        enableAction(false);
    }

    @SuppressWarnings("unused")
    private void closeDevice() {
        SunDriverApplication.getInstance().dirverToken.setState(0);
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<BaseResponseBean> updateViewAsyncCallback = new ProgressCallback<>(getBaseActivity());
        control.updateLocationAction(updateViewAsyncCallback, "8");
    }

    @Override
    public void onFirstUserVisible() {
        updateDirverStateUI();
        DirverToken dirverToken = SunDriverApplication.getInstance().dirverToken;
        if (dirverToken.getState() == 1) {
            enableAction(true);
            queryPrivate();
        }
        // setUpMap();
        super.onFirstUserVisible();
    }

    @Override
    public void onAttach(Context context) {
        context.registerReceiver(locationReceiver, new IntentFilter("LocationState"));
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        getContext().unregisterReceiver(locationReceiver);
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (mMap != null) {
            return;
        }
        mMap = map;
        Loger.e("map-" + map.toString());
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getMap().setMyLocationEnabled(locationEnabled);
            getMap().setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
                    getMap().setOnMyLocationChangeListener(null);
                }
            });
        } else {

        }
        // Show rationale and request permission.
        // getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new
        // LatLng(-33.8696, 151.2094), 10));
    }

    // @Override
    // public void onRequestPermissionsResult(int requestCode, String[]
    // permissions, int[] grantResults) {
    // if (requestCode == MY_LOCATION_REQUEST_CODE) {
    // if (permissions.length == 1 &&
    // permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
    // grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    // mMap.setMyLocationEnabled(true);
    // } else {
    // // Permission was denied. Display an error message.
    // }
    // }
    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).position(position)
                .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        getMap().addMarker(markerOptions);
        // getMap().
    }
}
