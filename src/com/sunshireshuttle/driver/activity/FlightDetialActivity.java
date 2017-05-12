package com.sunshireshuttle.driver.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.model.FlightInfo;

import net.iaf.framework.util.Loger;

/**
 * @author Heisenberg heisenberg.gong@koolpos.com>
 * @ClassName: FlightDetialActivity
 * @Description: 航班详情
 * @date 2016年8月29日 下午6:16:48
 */

public class FlightDetialActivity extends BaseActivity {

    private FlightInfo flightInfo;

    private TextView tv_fid, tv_flight_no, tv_flight_stats, tv_flight_last_update,
            tv_flight_deptime, tv_flight_airport, tv_flight_terminal, tv_flight_gate,
            tv_flight_arrival_deptime, tv_flight_arrival_airport, tv_flight_arrival_terminal, tv_flight_arrival_gate;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_flight_detial);
        flightInfo = (FlightInfo) getIntent().getSerializableExtra(FlightInfo.class.getName());
        Loger.d("flightInfo" + flightInfo.toString());
        TextView tv_toolbar_back = (TextView) findViewById(R.id.tv_toolbar_back);
        tv_toolbar_back.setText(R.string.back);
        tv_toolbar_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.tv_toolbar_title);
        title.setText("Flight #" + flightInfo.getFlight_num());
        tv_fid = (TextView) findViewById(R.id.tv_fid);
        tv_flight_no = (TextView) findViewById(R.id.tv_flight_no);
        tv_flight_stats = (TextView) findViewById(R.id.tv_flight_stats);
        tv_flight_last_update = (TextView) findViewById(R.id.tv_flight_last_update);
        tv_flight_deptime = (TextView) findViewById(R.id.tv_flight_deptime);
        tv_flight_airport = (TextView) findViewById(R.id.tv_flight_airport);
        tv_flight_terminal = (TextView) findViewById(R.id.tv_flight_terminal);
        tv_flight_gate = (TextView) findViewById(R.id.tv_flight_gate);
        tv_flight_arrival_deptime = (TextView) findViewById(R.id.tv_flight_arrival_deptime);
        tv_flight_arrival_airport = (TextView) findViewById(R.id.tv_flight_arrival_airport);
        tv_flight_arrival_terminal = (TextView) findViewById(R.id.tv_flight_arrival_terminal);
        tv_flight_arrival_gate = (TextView) findViewById(R.id.tv_flight_arrival_gate);

        tv_fid.setText(flightInfo.getFlight_id());
        tv_flight_no.setText(flightInfo.getFlight_num());
        tv_flight_stats.setText(flightInfo.getFlight_status());
        tv_flight_last_update.setText(flightInfo.getReg_date());

        tv_flight_deptime.setText(flightInfo.getLast_warning_dep());
        tv_flight_airport.setText(flightInfo.getDep_port());
        tv_flight_terminal.setText(flightInfo.getDep_terminal());
        tv_flight_gate.setText(flightInfo.getDep_gate());

        tv_flight_arrival_deptime.setText(flightInfo.getLast_warning_arr());
        tv_flight_arrival_airport.setText(flightInfo.getArr_port());
        tv_flight_arrival_terminal.setText(flightInfo.getArr_terminal());
        tv_flight_arrival_gate.setText(flightInfo.getArr_gate());
    }
}
