package com.sunshireshuttle.driver.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.model.RecentOrderItem;


public class HistoryOrderDetialActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detial);
        TextView tv_toolbar_back = (TextView) findViewById(R.id.tv_toolbar_back);
        tv_toolbar_back.setText("Recent Orders");
        tv_toolbar_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        try {
            RecentOrderItem orderItem = (RecentOrderItem) getIntent().getSerializableExtra(RecentOrderItem.class.getName());
            if (orderItem != null) {
                ((TextView) findViewById(R.id.tv_id)).setText(orderItem.getId());
                ((TextView) findViewById(R.id.tv_pickup_time)).setText(orderItem.getPickup_time());
                ((TextView) findViewById(R.id.tv_location_from)).setText(orderItem.getLocation_from());
                ((TextView) findViewById(R.id.tv_location_to)).setText(orderItem.getLocation_to());
                ((TextView) findViewById(R.id.tv_total)).setText("$" + orderItem.getTotal());
                ((TextView) findViewById(R.id.tv_receivable)).setText("$" + orderItem.getReceivable());
                ((TextView) findViewById(R.id.tv_customer_name)).setText(orderItem.getCustomer_name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
