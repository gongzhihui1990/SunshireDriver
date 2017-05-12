package com.sunshireshuttle.driver.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.adapter.MyBaseAdapter;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.model.SunshineTripNode;
import com.sunshireshuttle.driver.model.SunshineTripNode4Map;
import com.sunshireshuttle.driver.model.SunshineTripResponse;
import com.sunshireshuttle.driver.model.TripLogBean;
import com.sunshireshuttle.driver.model.TripLogsQueryResponse;
import com.sunshireshuttle.driver.model.TripRecordBean;
import com.sunshireshuttle.driver.utils.DateUtils;

import net.iaf.framework.util.EmptyUtil;
import net.iaf.framework.util.Loger;

import java.util.ArrayList;
import java.util.Date;


/**
 * This shows how to draw polylines on a map.
 */
public class SunshineTripLogsViewActivity extends BaseActivity {


    private TextView tv_toolbar_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_logs);
        final TripRecordBean tripRecordSimple = (TripRecordBean) getIntent().getSerializableExtra(TripRecordBean.class.getName());
        final String tripID = tripRecordSimple.getTrip_id();
        tv_toolbar_back = (TextView) findViewById(R.id.tv_toolbar_back);
        tv_toolbar_back.setText("Trip Log");
        tv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ProgressCallback<TripLogsQueryResponse> callback = new ProgressCallback<TripLogsQueryResponse>(SunshineTripLogsViewActivity.this) {
            @Override
            public void onPostExecute(TripLogsQueryResponse response) {
                super.onPostExecute(response);
                initTripLogs(response, tripRecordSimple);
            }
        };
        new DriverCommonControl().sendRequestForSearchTripHistoryLogWithTripID(callback, tripID);
    }

    ArrayList<LatLng> lngs = new ArrayList<>();

    private void initTripLogs(TripLogsQueryResponse response, TripRecordBean tripRecordSimple) {
        ListView list = (ListView) findViewById(R.id.list);
        ArrayList<TripLogBean> logList = response.getLog_list();
        RecordListAdapter adapter = new RecordListAdapter(logList);
        list.setAdapter(adapter);
    }

    class RecordListAdapter extends MyBaseAdapter {
        ArrayList<TripLogBean> logList = new ArrayList<>();

        RecordListAdapter(ArrayList<TripLogBean> logList) {
            this.logList.clear();
            if (logList != null) {
                this.logList.addAll(logList);
            }
        }

        @Override
        public int getCount() {
            return logList.size();
        }

        @Override
        public TripLogBean getItem(int position) {
            return logList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_item_trip_log_list, parent, false);
                holder = new Holder();
                holder.title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.content = (TextView) convertView.findViewById(R.id.tv_content);
                convertView.setTag(holder);
            }
            holder = (Holder) convertView.getTag();
            TripLogBean bean = getItem(position);
            holder.title.setText(bean.getMessage_type() + "  " + bean.getReg_date());
            holder.content.setText(bean.getMessage_text());
            return convertView;
        }

        class Holder {
            TextView title, content;
        }
    }
}