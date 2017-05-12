package com.sunshireshuttle.driver.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.dao.local.LocationRecordDao;
import com.sunshireshuttle.driver.model.BaseResponseBean;
import com.sunshireshuttle.driver.model.SunshineTripNode;
import com.sunshireshuttle.driver.model.SunshineTripNode4Map;
import com.sunshireshuttle.driver.model.SunshineTripResponse;
import com.sunshireshuttle.driver.model.TripRecordBean;
import com.sunshireshuttle.driver.utils.DateUtils;
import com.sunshireshuttle.driver.utils.UtilForTime;

import net.iaf.framework.util.EmptyUtil;
import net.iaf.framework.util.Loger;

import java.util.ArrayList;
import java.util.Date;


/**
 * This shows how to draw polylines on a map.
 */
public class SunshineTripDetialViewActivity extends BaseActivity
        implements OnMapReadyCallback {


    private TextView tv_toolbar_back;
    private ArrayList<SunshineTripNode4Map> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunshinetrip_detial);
        tv_toolbar_back = (TextView) findViewById(R.id.tv_toolbar_back);
        tv_toolbar_back.setText("Trip Record Detial");
        tv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final TripRecordBean tripRecordSimple = (TripRecordBean) getIntent().getSerializableExtra(TripRecordBean.class.getName());
        final String tripID = tripRecordSimple.getTrip_id();
        ProgressCallback<SunshineTripResponse> callback = new ProgressCallback<SunshineTripResponse>(SunshineTripDetialViewActivity.this) {
            @Override
            public void onPostExecute(SunshineTripResponse arg0) {
                super.onPostExecute(arg0);
                initTripData(arg0, tripRecordSimple);
            }
        };
        new DriverCommonControl().sendRequestForSearchCompleteTripWithID(callback, tripID);
    }

    ArrayList<LatLng> lngs = new ArrayList<>();

    private void initTripData(SunshineTripResponse tripResponse, TripRecordBean tripRecordSimple) {
        TextView tvTripid = (TextView) findViewById(R.id.tv_trip_id);
        TextView tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        TextView tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        TextView tvDuration = (TextView) findViewById(R.id.tv_duration);
        TextView tvDistance = (TextView) findViewById(R.id.tv_distance);
        TextView tvViewLog = (TextView) findViewById(R.id.tv_view_log);

        tvTripid.setText(tripRecordSimple.getTrip_id());
        tvStartTime.setText(tripResponse.getStart_point().getReg_date());
        tvEndTime.setText(tripResponse.getEnd_point().getReg_date());
        SunshineTripNode4Map start = new SunshineTripNode4Map(tripResponse.getStart_point());
        Date startTime = start.getTime_stamp();
        SunshineTripNode4Map end = new SunshineTripNode4Map(tripResponse.getEnd_point());
        Date endTime = end.getTime_stamp();
        tvDuration.setText(tripRecordSimple.getTime_consumed() + " mins");
        tvDistance.setText(tripRecordSimple.getMile_age_show() + " miles");
        tvViewLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SunshineTripDetialViewActivity.this, SunshineTripLogsViewActivity.class);
                intent.putExtras(getIntent());
                startActivity(intent);
            }
        });
        ArrayList<SunshineTripNode> nodes = tripResponse.getTrip_nodes();
        list = new ArrayList<>();
        for (SunshineTripNode node : nodes) {
            SunshineTripNode4Map node4Map = new SunshineTripNode4Map(node);
            list.add(node4Map);
        }
        for (int i = 0; i < list.size(); i++) {
            SunshineTripNode4Map bean = list.get(i);
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
//        PolylineOptions options =new PolylineOptions();
//        options.addAll(lngs);
//        options.width(3);
//        options.geodesic(true);
//        options.color(Color.BLUE);
//        map.addPolyline(options);


        for (int index = 0; index < list.size(); index++) {
            if (index != 0) {
                PolylineOptions options = new PolylineOptions();
                options.geodesic(true);
                options.width(5);
                double speed = SunshineTripNode4Map.
                        calculateMPHSpeedWithCurrentNode(list.get(index), list.get(index - 1));
                Loger.d("speed--" + index + "=" + speed);
                if (speed > 0.0 && speed < 5.0) {
                    options.color(Color.RED);
//                    lineView.strokeColor = [UIColor redColor];
                } else if (speed >= 5.0 && speed < 15.0) {
                    options.color(0xFFFF8800);
//                    lineView.strokeColor = [UIColor orangeColor];
                } else if (speed >= 15.0 && speed < 35.0) {
                    options.color(Color.YELLOW);
//                    lineView.strokeColor = [UIColor yellowColor];
                } else if (speed >= 35.0) {
                    options.color(Color.BLUE);
//                    lineView.strokeColor = [UIColor blueColor];
                }
                LatLng pointa = new LatLng(list.get(index).getLatitude(), list.get(index).getLongitude());
                LatLng pointb = new LatLng(list.get(index - 1).getLatitude(), list.get(index - 1).getLongitude());
                options.add(pointb);
                options.add(pointa);
                map.addPolyline(options);
            }

            SunshineTripNode4Map node = list.get(index);
            if ("1".equals(node.getAction())) {
//                node.get
                MarkerOptions mk = new MarkerOptions()
                        .title("Start")
                        .position(new LatLng(
                                Double.valueOf(node.getLatitude()), Double.valueOf(node.getLongitude())))
                        .snippet("Time:" +
                                DateUtils.formatDate(node.getTime_stamp(), DateUtils.dateFormatTimeHM));

                map.addMarker(mk);
            }
            if ("9".equals(node.getAction())) {
//                node.get
                MarkerOptions mk = new MarkerOptions()
                        .title("End")
                        .position(new LatLng(
                                Double.valueOf(node.getLatitude()), Double.valueOf(node.getLongitude())))
                        .snippet("Time:" +
                                DateUtils.formatDate(node.getTime_stamp(), DateUtils.dateFormatTimeHM));

                map.addMarker(mk);
            }

            if ("4".equals(node.getAction())) {
                if (index > 0 && index < list.size()
                        && list.get(index).getTime_stamp().getTime() - list.get(index - 1).getTime_stamp().getTime() > 120 * 1000
                        ) {
                    MarkerOptions mk = new MarkerOptions()
                            .title("STAY")
                            .position(new LatLng(
                                    Double.valueOf(node.getLatitude()), Double.valueOf(node.getLongitude())))
                            .snippet("From:" +
                                    DateUtils.formatDate(list.get(index - 1).getTime_stamp(), DateUtils.dateFormatTimeHM) +
                                    "To:" +
                                    DateUtils.formatDate(list.get(index).getTime_stamp(), DateUtils.dateFormatTimeHM)
                            );

                    map.addMarker(mk);
                }
            }

//            if ([node.action isEqualToString:@"4"] && [CYFunctionSet timeintervalFrom:last_node.time_stamp  to:node.time_stamp] > 120){
//                MKPointAnnotation *annotation = [[MKPointAnnotation alloc] init];
//                annotation.title = @"STAY";
//                [annotation setCoordinate:CLLocationCoordinate2DMake(node.latitude.doubleValue, node.longitude.doubleValue)];
//                [annotation setSubtitle:[NSString stringWithFormat:@"From:%@ To: %@", [CYFunctionSet convertDateToShortStr:[[_trip.tripNodes objectAtIndex:i-1] time_stamp]],[CYFunctionSet convertDateToShortStr:node.time_stamp]]];
//                [self.tripMap addAnnotation:annotation];
//
//                dispatch_async(dispatch_get_main_queue(), ^{
//                        [self.tripMap addOverlay:[self createRawLineWithCurrentNode:node forLastNode:last_node]];
//                });
//            }

//            if ([node.action isEqualToString:@"1"]) {
//                MKPointAnnotation *annotation = [[MKPointAnnotation alloc] init];
//                annotation.title = @"START";
//                [annotation setCoordinate:CLLocationCoordinate2DMake(node.latitude.doubleValue, node.longitude.doubleValue)];
//                [annotation setSubtitle:[NSString stringWithFormat:@"Time: %@", [CYFunctionSet convertDateToShortStr:node.time_stamp]]];
//                [self.tripMap addAnnotation:annotation];
//
//            }
        }

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
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lngs.get(lngs.size() - 1), 12));
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


    /**
     * Toggles the clickability of two polylines based on the state of the View that triggered this
     * call.
     * This callback is defined on the CheckBox in the layout for this Activity.
     */
    public void toggleClickability(View view) {
    }
}