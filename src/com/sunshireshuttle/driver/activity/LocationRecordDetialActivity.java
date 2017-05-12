package com.sunshireshuttle.driver.activity;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.dao.local.LocationRecordDao;
import com.sunshireshuttle.driver.model.LocationRecordBean;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import net.iaf.framework.util.EmptyUtil;


/**
 * This shows how to draw polylines on a map.
 */
public class LocationRecordDetialActivity extends BaseActivity
        implements OnSeekBarChangeListener, OnMapReadyCallback {

    private static final LatLng LAX = new LatLng(33.936524, -118.377686);

    LocationRecordDao dao = new LocationRecordDao();

    private ArrayList<LocationRecordBean> list;

    private ArrayList<LatLng> lngs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polyline);
        String tripID = getIntent().getStringExtra("tripID");
        list = dao.getListByTripId(tripID);
        lngs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            LocationRecordBean bean = list.get(i);
            LatLng lng = new LatLng(Double.valueOf(bean.getLatitude()), Double.valueOf(bean.getLongitude()));
            lngs.add(lng);
        }
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        map.setContentDescription("Google Map with polylines.");
        // A simple polyline with the default options from Melbourne-Adelaide-Perth.
        PolylineOptions options = new PolylineOptions();
        options.addAll(lngs);
        options.width(3);
        options.geodesic(true);
        options.color(Color.BLUE);
        map.addPolyline(options);
//        for (LatLng lngPoint: lngs) {
//		}
        // A geodesic polyline that goes around the world.
//        mClickablePolyline = map.addPolyline((new PolylineOptions())
//                .add(LHR, AKL, LAX, JFK, LHR)
//                .width(5)
//                .color(Color.BLUE)
//                .geodesic(true));
//                .clickable(mClickabilityCheckbox.isChecked()));

        // Rectangle centered at Sydney.  This polyline will be mutable.
//        int radius = 5;
//        PolylineOptions options = new PolylineOptions()
//                .add(new LatLng(SYDNEY.latitude + radius, SYDNEY.longitude + radius))
//                .add(new LatLng(SYDNEY.latitude + radius, SYDNEY.longitude - radius))
//                .add(new LatLng(SYDNEY.latitude - radius, SYDNEY.longitude - radius))
//                .add(new LatLng(SYDNEY.latitude - radius, SYDNEY.longitude + radius))
//                .add(new LatLng(SYDNEY.latitude + radius, SYDNEY.longitude + radius));
//        int color = Color.HSVToColor(
//                mAlphaBar.getProgress(), new float[]{mColorBar.getProgress(), 1, 1});
//        mMutablePolyline = map.addPolyline(options
//                .color(color)
//                .width(mWidthBar.getProgress()));
//                .clickable(mClickabilityCheckbox.isChecked()));

        // Move the map so that it is centered on the mutable polyline.

        if (!EmptyUtil.isEmpty(lngs)) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lngs.get(0), 18));
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LAX, 18));
        }
        // Add a listener for polyline clicks that changes the clicked polyline's color.
//        map.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
//            @Override
//            public void onPolylineClick(Polyline polyline) {
//                // Flip the values of the r, g and b components of the polyline's color.
//                int strokeColor = polyline.getColor() ^ 0x00ffffff;
//                polyline.setColor(strokeColor);
//            }
//        });
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    /**
     * Toggles the clickability of two polylines based on the state of the View that triggered this
     * call.
     * This callback is defined on the CheckBox in the layout for this Activity.
     */
    public void toggleClickability(View view) {
    }
}