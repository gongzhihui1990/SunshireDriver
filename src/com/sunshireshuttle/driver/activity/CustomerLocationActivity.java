package com.sunshireshuttle.driver.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.model.CurrentOrderItem;
import com.sunshireshuttle.driver.model.CustomerLocation;
import com.sunshireshuttle.driver.model.GetCustomerLocationResponse;
import com.sunshireshuttle.driver.utils.UtilForTime;

import net.iaf.framework.util.Loger;
import net.iaf.framework.util.TimeHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.sunshireshuttle.driver.R.id.tv_toolbar_back;
import static net.iaf.framework.app.BaseApplication.getContext;

public class CustomerLocationActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView tv_update_time;
    private TextView tv_req_time;

    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        getMap().clear();
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        getMap().addMarker(markerOptions);
    }

    private void onDrawCustom(CustomerLocation customerLocation) {
        if (getMap() != null) {
            IconGenerator iconFactory = new IconGenerator(getContext());
            ImageView iv = new ImageView(getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            iv.setLayoutParams(params);
            iv.setImageDrawable(getContext().getResources().getDrawable(R.drawable.walter_white));
            iconFactory.setContentView(iv);
            LatLng latlng =
                    new LatLng(customerLocation.getLatitude(), customerLocation.getLongitude());
            addIcon(iconFactory, "Default", latlng);
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
            onRenderCustomerLocationPanel(customerLocation);
        }
    }

    private CustomerLocation customerLocation;
    private CurrentOrderItem currentOrderItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_location);
        currentOrderItem = (CurrentOrderItem) getIntent().getSerializableExtra(CurrentOrderItem.class.getName());
        if (currentOrderItem == null) {
            finish();
            return;
        }
        TextView tv_toolbar_back = (TextView) findViewById(R.id.tv_toolbar_back);
        tv_toolbar_back.setText("Order #:" + currentOrderItem.getId());
        tv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        View reflashView = findViewById(R.id.reflash);
        reflashView.setVisibility(View.VISIBLE);
        reflashView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchCustomerLocation(currentOrderItem.getId());
            }
        });
        tv_update_time = (TextView) findViewById(R.id.tv_update_time);
        tv_req_time = (TextView) findViewById(R.id.tv_req_time);
        TextView tv_cus_name = (TextView) findViewById(R.id.tv_cus_name);
        tv_cus_name.setText(currentOrderItem.getCustomer_nameb());

        customerLocation = (CustomerLocation) getIntent().getSerializableExtra(CustomerLocation.class.getName());
        if (customerLocation == null) {
//            onRenderCustomerLocationPanel(customerLocation)
            searchCustomerLocation(currentOrderItem.getId());
        }
        setUpMap();
    }

    private void searchCustomerLocation(String orderId) {
        ProgressCallback<GetCustomerLocationResponse> updateViewAsyncCallback = new ProgressCallback<GetCustomerLocationResponse>(CustomerLocationActivity.this) {
            public void onPreExecute() {
                setProgressMessage("querying...");
                super.onPreExecute();
            }

            ;

            public void onPostExecute(GetCustomerLocationResponse response) {
                super.onPostExecute(response);
                customerLocation = response.getLocation();

            }

            ;
        };
        new DriverCommonControl().requestCustomerLocationWithOrderID(updateViewAsyncCallback, orderId);
    }

    private void onRenderCustomerLocationPanel(CustomerLocation location) {
        String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format1 = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        String updateTime = location.getReg_date();
        String reqTime = TimeHelper.getCurrentTimeStr();
        try {
            updateTime = format2.format(format1.parse(updateTime));
            reqTime = format2.format(format1.parse(reqTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_update_time.setText("Last update:" + updateTime);
        tv_req_time.setText("Last request:" + reqTime);
//        tv_update_time=
//                tv_req_time
    }

    private void setUpMap() {
        SupportMapFragment map = new SupportMapFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map, map);
        transaction.commit();
        map.getMapAsync(this);
    }

    private GoogleMap getMap() {
        return mMap;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (mMap != null) {
            return;
        }
        mMap = map;
        Loger.e("map-" + map.toString());
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getMap().setMyLocationEnabled(true);
            getMap().setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                private boolean moved = false;
                private double fix = 0;
                private CustomerLocation csLoction;

                @Override
                public void onMyLocationChange(Location location) {
                    LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (!moved) {
                        moved = true;
                        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
                    }
                    if (customerLocation != null && !customerLocation.equals(csLoction)) {
                        csLoction = customerLocation;
                        onDrawCustom(csLoction);
                    }

                }
            });
        } else {
            Loger.d("error");
        }
        // Show rationale and request permission.
//        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.8696, 151.2094), 10));
    }

    @Override
    protected void onDestroy() {
        if (getMap() != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            getMap().setMyLocationEnabled(false);
        }
        super.onDestroy();
    }

}
