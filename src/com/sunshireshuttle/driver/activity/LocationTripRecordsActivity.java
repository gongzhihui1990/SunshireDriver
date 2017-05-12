package com.sunshireshuttle.driver.activity;

import java.util.ArrayList;

import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.dao.local.LocationRecordDao;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.iaf.framework.util.Loger;

public class LocationTripRecordsActivity extends BaseActivity {
    RecordListAdapter adapter;
    LocationRecordDao dao = new LocationRecordDao();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_locationl_trip_records);
        TextView tv_toolbar_back = (TextView) findViewById(R.id.tv_toolbar_back);
        tv_toolbar_back.setText("Recent Orders");
        final ListView lv_records = (ListView) findViewById(R.id.lv_records);
        tv_toolbar_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_toolbar_back.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                dao.delete();
                adapter = new RecordListAdapter();
                lv_records.setAdapter(adapter);
                return false;
            }
        });
        adapter = new RecordListAdapter();
        lv_records.setAdapter(adapter);
        lv_records.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String tripID = adapter.getItem(position);
                Intent intent = new Intent(LocationTripRecordsActivity.this, LocationRecordDetialActivity.class);
                intent.putExtra("tripID", tripID);
                startActivity(intent);
            }
        });

    }

    class RecordListAdapter extends BaseAdapter {
        ArrayList<String> tripIds = new ArrayList<>();

        public RecordListAdapter() {
            tripIds = dao.getAllTripId();
            Loger.d("tripIds:" + tripIds.size());
        }

        @Override
        public int getCount() {
            return tripIds.size();
        }

        @Override
        public String getItem(int position) {
            return tripIds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(LocationTripRecordsActivity.this).inflate(R.layout.layout_item_trip_list, parent, false);
                holder = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }
            holder = (TextView) convertView.getTag();
            holder.setText(getItem(position));
            return convertView;
        }

    }
}
