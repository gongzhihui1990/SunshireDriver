package com.sunshireshuttle.driver.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.adapter.SmsContentAdapter;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.model.BaseResponseBean;
import com.sunshireshuttle.driver.model.CurrentOrderItem;
import com.sunshireshuttle.driver.model.SmsContent;
import com.sunshireshuttle.driver.widget.Actions4SimpleDlg;

import net.iaf.framework.exception.IException;
import net.iaf.framework.util.Loger;

import java.util.ArrayList;

public class SMSTableActivity extends BaseActivity {

    private final static ArrayList<SmsContent> lateArray = new ArrayList<>();
    private final static ArrayList<SmsContent> addressArray = new ArrayList<>();
    private final static ArrayList<SmsContent> situationArray = new ArrayList<>();
    private final static ArrayList<SmsContent> airPort = new ArrayList<>();
    private TextView tv_late, tv_2, tv_3, tv_4;
    private SmsContentAdapter adapter;
    private ListView list_sms;
    private GridView grid_sms;

    private String productId;

    private String createGreeting(CurrentOrderItem orderItem) {
        return "Dear " + orderItem.getCustomer_nameb() + ", ";
    }

    private void initArray(final CurrentOrderItem orderItem) {
        lateArray.clear();
        addressArray.clear();
        situationArray.clear();
        airPort.clear();

        String Taffic = String.format("%s Sunshire Driver ETA delay. Due to:%s . New ETA: ", createGreeting(orderItem), "[Taffic]");
        String Weather = String.format("%s Sunshire Driver ETA delay. Due to:%s . New ETA: ", createGreeting(orderItem), "[Weather]");
        String RoadWork = String.format("%s Sunshire Driver ETA delay. Due to:%s . New ETA: ", createGreeting(orderItem), "[Road Work]");
        String Caraccident = String.format("%s Sunshire Driver ETA delay. Due to:%s . New ETA: ", createGreeting(orderItem), "[Car accident]");
        lateArray.add(new SmsContent("Taffic", Taffic));
        lateArray.add(new SmsContent("Weather", Weather));
        lateArray.add(new SmsContent("Road Work", RoadWork));
        lateArray.add(new SmsContent("Car accident", Caraccident));

        String addressVerification = String.format("%s Can you please verify your address, is it [%s]?", createGreeting(orderItem), orderItem.getLocation_from());
        String entranceCode = String.format("%s SunshiRe Shuttle driver is at the gate, can you please text us the entrance code?", createGreeting(orderItem));
        addressArray.add(new SmsContent("Address verification", addressVerification));
        addressArray.add(new SmsContent("Entrance code", entranceCode));

        String waitLimit = String.format("%s SunshiRe Shuttle driver has been waiting for you for 10 minutes. Please be advised, driver can be instructed to leave or there's a wait time charge involved after [***]", createGreeting(orderItem));
        String waitingCharge = String.format("%splease reply “ok” to authorize a charge for driver's wait time. It is $10 for each 15 minute.", createGreeting(orderItem));
        situationArray.add(new SmsContent("Wait time limit", waitLimit));
        situationArray.add(new SmsContent("Wait time charge", waitingCharge));


        airPort.add(new SmsContent(getString(R.string.sms_airport_title_1), String.format("%sSunshine Shuttle driver has left the remote parking lot and is currently driving towards the inner curb.", createGreeting(orderItem))));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_2), String.format("%sSunshiRe Shuttle driver is approaching the meeting point. ", createGreeting(orderItem))));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_3), String.format("%scould you please describe what you are wearing. This will help the driver find you easily.", createGreeting(orderItem))));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_4), String.format("%sOur vehicle info is [***] ", createGreeting(orderItem))));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_5), String.format("%sSunshire Shuttle driver is experiencing heavy traffic in the airport. Your pick up time could be delayed up to 15 minutes.", createGreeting(orderItem))));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_6), String.format("%sSunshire Shuttle driver has arrived at curbside.", createGreeting(orderItem))));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_7), String.format("%sSunshire Shuttle driver cannot stay long at curbside due to airport security and is circling back to the curb now. ", createGreeting(orderItem))));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_8), String.format("%sSunshire Shuttle driver is experiencing heavy traffic in the airport. Your pick up time could be delayed up to 15 minutes.", createGreeting(orderItem))));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_9), String.format("%sSunshire shuttle driver is on standby and is waiting at a remote parking lot for your update on your current status.", createGreeting(orderItem))));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_10), String.format("%splease reply '1' once you have deplaned, '2' once you have acquired your luggage and '3' once you have arrived at the inner curb. Please do not go across to the center island, thank you.", createGreeting(orderItem))));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_11), String.format("Thank you for the info, driver acknowledged.")));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_12), String.format("%splease wait at the inner curb and tell us which part of the terminal you are at. (e.g. The glass door behind you may be marked L1­-10).", createGreeting(orderItem))));
        airPort.add(new SmsContent(getString(R.string.sms_airport_title_13), String.format("%splease wait at the inner curb and tell us which part of the terminal you are at. (e.g. There may be a sign above you saying Alaskan Airlines).", createGreeting(orderItem))));

    }

    CurrentOrderItem orderItem;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Uri uri = Uri.parse(getIntent().getStringExtra(Uri.class.getName()));
        Loger.d("uri:" + uri.toString());
        orderItem = (CurrentOrderItem) getIntent()
                .getSerializableExtra(CurrentOrderItem.class.getName());
        if (orderItem != null) {
            productId = orderItem.getId();
            initArray(orderItem);
        }
        setContentView(R.layout.activity_sms_table);
        TextView tv_toolbar_back = (TextView) findViewById(R.id.tv_toolbar_back);
        tv_toolbar_back.setText("SMS Customer");
        tv_toolbar_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_late = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        list_sms = (ListView) findViewById(R.id.list_sms);
        grid_sms = (GridView) findViewById(R.id.grid_sms);
        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!v.isSelected()) {
                    tv_late.setSelected(false);
                    tv_2.setSelected(false);
                    tv_3.setSelected(false);
                    tv_4.setSelected(false);
                    v.setSelected(true);
                    switch (v.getId()) {
                        case R.id.tv_1:
                            adapter.setList(lateArray, SmsContentAdapter.ModeList);
                            list_sms.setVisibility(View.VISIBLE);
                            grid_sms.setVisibility(View.GONE);
                            break;
                        case R.id.tv_2:
                            list_sms.setVisibility(View.VISIBLE);
                            grid_sms.setVisibility(View.GONE);
                            adapter.setList(addressArray, SmsContentAdapter.ModeList);
                            break;
                        case R.id.tv_3:
                            list_sms.setVisibility(View.VISIBLE);
                            grid_sms.setVisibility(View.GONE);
                            adapter.setList(situationArray, SmsContentAdapter.ModeList);
                            break;
                        case R.id.tv_4:
                            list_sms.setVisibility(View.GONE);
                            grid_sms.setVisibility(View.VISIBLE);
                            adapter.setList(airPort, SmsContentAdapter.ModeGrid);
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        tv_late.setOnClickListener(listener);
        tv_2.setOnClickListener(listener);
        tv_3.setOnClickListener(listener);
        tv_4.setOnClickListener(listener);
        tv_late.setSelected(true);

        adapter = new SmsContentAdapter(lateArray, this);
        list_sms.setVisibility(View.VISIBLE);
        grid_sms.setVisibility(View.GONE);
        DataSetObserver observer = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                TextView tvSend = (TextView) findViewById(R.id.send);
                final String message = adapter.getSelectText();
                if (!TextUtils.isEmpty(message)) {
                    tvSend.setVisibility(View.VISIBLE);
                    tvSend.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendSms(message);
                        }
                    });
                } else {
                    tvSend.setVisibility(View.GONE);
                }
            }
        };
        adapter.registerDataSetObserver(observer);
        list_sms.setAdapter(adapter);
        grid_sms.setAdapter(adapter);
    }

    private void sendSms(String message) {
//        Uri uri = Uri.parse(getIntent().getStringExtra(Uri.class.getName()));
//        Loger.d("uri:" + uri.toString());
//        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
//        sendIntent.putExtra("sms_body", message);
//        startActivity(sendIntent);
        sendSmsByMessage(message);
    }

    private void sendSmsByMessage(String message) {
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<BaseResponseBean> callback = new ProgressCallback<BaseResponseBean>(this) {
            @Override
            public void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            public void onException(IException exception) {
                super.onException(exception);
            }

            @Override
            public void onPostExecute(BaseResponseBean arg0) {
//                Intent intent=new Intent(SMSTableActivity.this,MessagerActivity.class);
//                intent.putExtra(CurrentOrderItem.class.getName(),orderItem);
//                startActivity(intent);
                Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                actions4SimpleDlg.title = "Success";
                actions4SimpleDlg.message = "Message sent";
                actions4SimpleDlg.cancelble = false;
                actions4SimpleDlg.btn2 = "Ok";
                SMSTableActivity.this.showDialog(actions4SimpleDlg);
                super.onPostExecute(arg0);
            }
        };
        control.sendMessageWithTripID(callback, productId, message);

    }
}
