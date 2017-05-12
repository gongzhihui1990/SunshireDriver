package com.sunshireshuttle.driver.activity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.constans.Action;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.dao.DBHelperImpl;
import com.sunshireshuttle.driver.model.BaseResponseBean;
import com.sunshireshuttle.driver.model.CurrentOrderItem;
import com.sunshireshuttle.driver.model.CustomerLocation;
import com.sunshireshuttle.driver.model.FlightInfo;
import com.sunshireshuttle.driver.model.FlightInfoResponse;
import com.sunshireshuttle.driver.model.GetCustomerLocationResponse;
import com.sunshireshuttle.driver.widget.Actions4SimpleDlg;
import com.sunshireshuttle.driver.widget.SimpleDialogFragment;
import com.sunshireshuttle.driver.widget.SimpleToast;

import net.iaf.framework.exception.DBException;
import net.iaf.framework.exception.IException;
import net.iaf.framework.exception.ServerException;
import net.iaf.framework.util.Loger;

import java.text.SimpleDateFormat;

/**
 * @author Heisenberg heisenberg.gong@koolpos.com>
 * @ClassName: CurrentOrderDetialActivity
 * @Description: 未完成的订单详情
 * @date 2016年8月30日 上午9:16:24
 */

public class CurrentOrderDetialActivity extends BaseActivity {

    private CurrentOrderItem localOrder;

    private boolean showDialog;
    private DBHelperImpl dbHelperImpl = new DBHelperImpl();
    private String cacheKey;
    private int alertType = -1;
    private CurrentOrderItemState orderItemState = new CurrentOrderItemState();

    private View ll_sms_cus;
    private View ll_call_cus;
    private View ll_eta_sent;
    private View ll_cob;
    private View ll_cad;
    private View ll_address_pick;
    private View ll_arrival;
    private View ll_way_bill;
    private View ll_realtime_location;
    private View ll_flight_info;
    private View ll_cob_warning;
    private View ll_call_manager;
    private View ll_send_link;
    private View ll_messager;
    private TextView tv_note;
    private TextView tv_receive_sms_1;
    private TextView tv_receivable;
    private TextView tv_receive_sms;
    private View ll_address_drop;
    private TextView tv_toolbar_back;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order_detial);
        tv_toolbar_back = (TextView) findViewById(R.id.tv_toolbar_back);
        tv_toolbar_back.setText(R.string.back);
        tv_toolbar_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_address_pick = findViewById(R.id.ll_address_pick);
        ll_address_drop = findViewById(R.id.ll_address_drop);
        ll_eta_sent = findViewById(R.id.ll_eta);
        ll_cob = findViewById(R.id.ll_cob);
        ll_arrival = findViewById(R.id.ll_arrival);
        ll_cad = findViewById(R.id.ll_cad);
        ll_sms_cus = findViewById(R.id.ll_sms_cus);
        ll_call_cus = findViewById(R.id.ll_call_cus);
        ll_cob_warning = findViewById(R.id.ll_cob_warning);
        ll_send_link = findViewById(R.id.ll_send_link);
        ll_messager = findViewById(R.id.ll_messager);
        ll_call_manager = findViewById(R.id.ll_call_manager);
        ll_way_bill = findViewById(R.id.ll_way_bill);
        ll_flight_info = findViewById(R.id.ll_flight_info);
        ll_realtime_location = findViewById(R.id.ll_realtime_location);
        try {
            showDialog = getIntent().getBooleanExtra("showDialog", false);
            alertType = getIntent().getIntExtra("alertType", -1);
            localOrder = (CurrentOrderItem) getIntent().getSerializableExtra(CurrentOrderItem.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CurrentOrderItem.class.getName(), localOrder);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        localOrder = (CurrentOrderItem) savedInstanceState.getSerializable(CurrentOrderItem.class.getName());
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     * @author lizhgb
     * @Date 2015-10-14 上午10:38:04
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    private static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    @SuppressLint("SimpleDateFormat")
    private void initView() {
        cacheKey = "CurrentOrderItem-" + localOrder.getId();
        TextView title = (TextView) findViewById(R.id.tv_toolbar_title);
        title.setText("Order #" + localOrder.getId());
        title.setTextColor(getResources().getColor(R.color.blue));
        // tv_toolbar_back.setOnLongClickListener(new OnLongClickListener() {
        //
        // @Override
        // public boolean onLongClick(View v) {
        // try {
        // dbHelperImpl.setDataToLocal(new CurrentOrderItemState().toString(),
        // cacheKey);
        // orderItemState.setData(new CurrentOrderItemState());
        // SimpleToast.ToastMessage("clear");
        // } catch (DBException e) {
        // SimpleToast.ToastError(e);
        // e.printStackTrace();
        // }
        // return false;
        // }
        // });
        // setCuse
        try {
//			final String cacheGson = (String) dbHelperImpl.getDataFromLocal(cacheKey);
            CurrentOrderItemState orderItemStateData = new Gson().fromJson((String) dbHelperImpl.getDataFromLocal(cacheKey), CurrentOrderItemState.class);
            if (orderItemStateData != null) {
                orderItemState.setData(orderItemStateData);
            } else {
                orderItemState.setData(orderItemState);
            }
            title.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                        vib.vibrate(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String cacheGsonStr = "no local data cached";
                    try {
                        cacheGsonStr = (String) dbHelperImpl.getDataFromLocal(cacheKey);
                        cacheGsonStr = formatJson(cacheGsonStr);
                    } catch (DBException e) {
                        e.printStackTrace();
                    }
                    showDialog("cache data:", cacheGsonStr);
                }
            });
            title.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    String cacheGsonStr = "no local data cached";
                    try {
                        cacheGsonStr = (String) dbHelperImpl.getDataFromLocal(cacheKey);
                        cacheGsonStr = formatJson(cacheGsonStr);
                    } catch (DBException e) {
                        e.printStackTrace();
                    }
                    try {
                        Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                        vib.vibrate(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                    actions4SimpleDlg.title = "delete cache data?";
                    actions4SimpleDlg.message = cacheGsonStr;
                    actions4SimpleDlg.cancelble = true;
                    actions4SimpleDlg.confirmListener = new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                dbHelperImpl.setDataToLocal(new CurrentOrderItemState().toString(), cacheKey);
                            } catch (DBException e) {
                                e.printStackTrace();
                            }
                            orderItemState.setData(new CurrentOrderItemState());
                            SimpleToast.ToastMessage("clear cache data success");
                        }
                    };
                    showDialog(actions4SimpleDlg);
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            SimpleToast.ToastError(e);
        }
        if (showDialog) {
            Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
            switch (alertType) {
                case CurrentOrderItem.AlertCob:
                    actions4SimpleDlg.message = "Send COB message now?";
                    actions4SimpleDlg.cancelble = false;
                    actions4SimpleDlg.confirmListener = new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            processCOBTimeSelection();
                        }
                    };
                    actions4SimpleDlg.otherListener = new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            processCOBExtension();
                        }
                    };
                    actions4SimpleDlg.btn1 = "Close";
                    actions4SimpleDlg.btn2 = "Send COB";
                    actions4SimpleDlg.btn3 = "Extend COB";
                    CurrentOrderDetialActivity.this.showDialog(actions4SimpleDlg);
                    break;
                case CurrentOrderItem.AlertEta:
                    actions4SimpleDlg.message = "Send ETA message now?";
                    actions4SimpleDlg.cancelble = false;
                    actions4SimpleDlg.btn1 = "Close";
                    actions4SimpleDlg.btn2 = "Send ETA";
                    actions4SimpleDlg.confirmListener = new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            processETATimeSelection();
                        }
                    };
                    showDialog(actions4SimpleDlg);
                    break;

                default:
                    break;
            }
        }
        ((TextView) findViewById(R.id.tv_customer_name)).setText(localOrder.getCustomer_nameb());
        ((TextView) findViewById(R.id.tv_passengers)).setText(localOrder.getPassenger());
        ((TextView) findViewById(R.id.tv_baggages)).setText(localOrder.getBaggage());
        ((TextView) findViewById(R.id.tv_address_pick)).setText(localOrder.getLocation_from());
        ((TextView) findViewById(R.id.tv_address_drop)).setText(localOrder.getLocation_to());
        String time = localOrder.getPickup_time();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        SimpleDateFormat format2 = new SimpleDateFormat("MM/dd HH:mm");
        try {
            time = format2.format(format1.parse(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.tv_pickup_time)).setText(time);
        tv_receivable = (TextView) findViewById(R.id.tv_receivable);
        tv_receive_sms_1 = (TextView) findViewById(R.id.tv_receive_sms_1);
        tv_receive_sms = (TextView) findViewById(R.id.tv_receive_sms);
        tv_note = (TextView) findViewById(R.id.tv_note_title);
        String note = localOrder.getNote();
        if (!TextUtils.isEmpty(note)) {
            tv_note.setText(note);
            tv_note.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        } else {
            tv_note.setText("Empty Note");
            tv_note.setTextColor(getResources().getColor(android.R.color.black));
        }
        if ("1".equals(localOrder.getIs_text_okb())) {
            Drawable left = getResources().getDrawable(R.drawable.o_detial_receive_message_yes);
            left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
            tv_receive_sms_1.setCompoundDrawables(left, null, null, null);
            tv_receive_sms.setText("YES");
        } else {
            Drawable left = getResources().getDrawable(R.drawable.o_detial_receive_message_no);
            left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
            tv_receive_sms_1.setCompoundDrawables(left, null, null, null);
            tv_receive_sms.setText("NO");
        }
        tv_receivable.setText(localOrder.getReceivable());
        if (!localOrder.getReceivable().equals("0.00")) {
            tv_receivable.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        ll_address_pick.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                performGoogle(localOrder.getLocation_from());
            }
        });
        ll_address_drop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                performGoogle(localOrder.getLocation_to());
            }
        });
        ll_way_bill.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrentOrderDetialActivity.this, WebViewActivity.class);
                intent.putExtra("id", localOrder.getId());
                startActivity(intent);
            }
        });
        ll_realtime_location.setOnClickListener(new OnClickListener() {
            private void searchCustomerLocation() {
                ProgressCallback<GetCustomerLocationResponse> updateViewAsyncCallback = new ProgressCallback<GetCustomerLocationResponse>(
                        CurrentOrderDetialActivity.this) {
                    public void onPreExecute() {
                        setProgressMessage("querying...");
                        super.onPreExecute();
                    }

                    public void onPostExecute(GetCustomerLocationResponse response) {
                        super.onPostExecute(response);
                        CustomerLocation location = response.getLocation();
                        Intent intent = new Intent(CurrentOrderDetialActivity.this, CustomerLocationActivity.class);
                        intent.putExtra(CustomerLocation.class.getName(), location);
                        intent.putExtra(CurrentOrderItem.class.getName(), localOrder);
                        startActivity(intent);
                    }
                };
                new DriverCommonControl().requestCustomerLocationWithOrderID(updateViewAsyncCallback,
                        localOrder.getId());
            }

            @Override
            public void onClick(View v) {
                searchCustomerLocation();
            }
        });

        ll_flight_info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearchFlight();
            }

            private void performSearchFlight() {
                final String flightInfoId = localOrder.getFlight_info_id();
                if (TextUtils.isEmpty(flightInfoId)) {
                    showDialog("Error", "Cannot find flight...");
                } else {
                    ProgressCallback<FlightInfoResponse> updateViewAsyncCallback = new ProgressCallback<FlightInfoResponse>(
                            CurrentOrderDetialActivity.this) {
                        public void onPostExecute(FlightInfoResponse flightInfoResponse) {
                            super.onPostExecute(flightInfoResponse);
                            FlightInfo flightInfo = flightInfoResponse.getFlight();
                            if (flightInfo != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.addCategory("com.sunshireshuttle");
                                intent.putExtra(FlightInfo.class.getName(), flightInfo);
                                try {
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        ;
                    };
                    commonControl.sendSearchRequestWithFlight(updateViewAsyncCallback, flightInfoId);
                }

            }
        });
        findViewById(R.id.ll_note).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        ll_sms_cus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String phoneNumber = localOrder.getSystem_phone();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    Intent intent = new Intent(Action.ACTION_SENDTO);
                    intent.putExtra(Uri.class.getName(), "smsto:" + phoneNumber);
                    intent.putExtra(CurrentOrderItem.class.getName(), localOrder);
                    startActivity(intent);
                } else {
                    SimpleToast.ToastMessage("Number not set");
                }
            }
        });

        ll_call_cus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String phoneNumber = localOrder.getCustomer_phoneb();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phoneNumber);
                    intent.setData(data);
                    startActivity(intent);
                }
            }
        });
        ll_cob_warning.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                actions4SimpleDlg.title = "COB Warning";
                actions4SimpleDlg.message = "Send COB Warning?";
                actions4SimpleDlg.cancelble = true;
                actions4SimpleDlg.confirmListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performCobWarning();
                    }

                    private void performCobWarning() {
                        DriverCommonControl control = new DriverCommonControl();
                        ProgressCallback<BaseResponseBean> updateViewAsyncCallback = new ProgressCallback<BaseResponseBean>(
                                CurrentOrderDetialActivity.this) {
                            @Override
                            public void onPostExecute(BaseResponseBean arg0) {
                                if (arg0.getStatus()) {
                                    showDialog("COB Warning", "COB Warning Sent!");
                                }
                                super.onPostExecute(arg0);
                            }

                            @Override
                            public void onException(IException exception) {
                                super.onException(exception);
                                if (!(exception instanceof ServerException)) {
                                    showActionFailWithAction("COBWARN");
                                }
                            }
                        };
                        control.sentCOBWARN(updateViewAsyncCallback, localOrder.getId());
                    }

                };
                showDialog(actions4SimpleDlg);
            }
        });
        ll_call_manager.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String phoneNumber = localOrder.getAlert_phone();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phoneNumber);
                    intent.setData(data);
                    startActivity(intent);
                } else {
                    SimpleToast.ToastMessage("Number not set");
                }
            }
        });

        ll_eta_sent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                actions4SimpleDlg.title = "ETA";
                actions4SimpleDlg.message = "Send ETA?";
                actions4SimpleDlg.cancelble = true;
                actions4SimpleDlg.confirmListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performETA();
                    }

                    private void performETA() {
                        if (orderItemState.getEta_status().equals("SUCCESS")) {
                            Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                            actions4SimpleDlg.title = "ETA";
                            actions4SimpleDlg.message = "Customer has already received ETA.";
                            actions4SimpleDlg.btn2 = "Continue";
                            actions4SimpleDlg.confirmListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    processETATimeSelection();
                                }
                            };
                            showDialog(actions4SimpleDlg);
                        } else {
                            processETATimeSelection();
                        }
                    }

                };
                showDialog(actions4SimpleDlg);
            }
        });
        ll_arrival.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                actions4SimpleDlg.title = "ARRIVAL";
                actions4SimpleDlg.message = "Send ARRIVAL?";
                actions4SimpleDlg.cancelble = true;
                actions4SimpleDlg.confirmListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performCOB();
                    }

                    private void performCOB() {
                        if (orderItemState.getArrival_status().equals("SUCCESS")) {
                            Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                            actions4SimpleDlg.title = "ARRIVAL";
                            actions4SimpleDlg.message = "Customer has already received ARRIVAL message.";
                            actions4SimpleDlg.btn2 = "Continue";
                            actions4SimpleDlg.confirmListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    processArrivalSelection();
                                }
                            };
                            showDialog(actions4SimpleDlg);
                            return;
                        } else {
                            processArrivalSelection();
                        }
                    }

                };
                showDialog(actions4SimpleDlg);
            }
        });
        ll_cob.setOnClickListener(new OnClickListener() {

            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {
                if (!orderItemState.getEta_status().equals("SUCCESS")) {
                    Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                    actions4SimpleDlg.title = "COB";
                    actions4SimpleDlg.message = "Please send ETA first.";
                    actions4SimpleDlg.cancelble = true;
                    actions4SimpleDlg.btn1 = "OK";
                    actions4SimpleDlg.btn2 = "Forced send COB";
                    actions4SimpleDlg.textColorRes = android.R.color.holo_red_light;
                    actions4SimpleDlg.confirmListener = new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            performCOB();
                        }

                        private void performCOB() {
                            if (orderItemState.getCob_status().equals("SUCCESS")) {
                                showDialog("COB", "Customer has already received COB message.");
                            } else {
                                processCOBTimeSelection();
                            }
                        }

                    };
                    showDialog(actions4SimpleDlg);
                    return;
                }
                Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                actions4SimpleDlg.title = "COB";
                actions4SimpleDlg.message = "Send COB?";
                actions4SimpleDlg.cancelble = true;
                actions4SimpleDlg.confirmListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performCOB();
                    }

                    private void performCOB() {
                        if (orderItemState.getCob_status().equals("SUCCESS")) {
                            showDialog("COB", "Customer has already received COB message.");
                        } else {
                            processCOBTimeSelection();
                        }
                    }

                };
                showDialog(actions4SimpleDlg);
            }
        });
        ll_cad.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!orderItemState.getCob_status().equals("SUCCESS")) {
                    Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                    actions4SimpleDlg.title = "CAD";
                    actions4SimpleDlg.message = "Please send COB first.";
                    actions4SimpleDlg.cancelble = true;
                    actions4SimpleDlg.btn1 = "OK";
                    actions4SimpleDlg.btn2 = "Forced send CAD";
                    actions4SimpleDlg.textColorRes = android.R.color.holo_red_light;
                    actions4SimpleDlg.confirmListener = new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            performCAD();
                        }

                        private void performCAD() {
                            if (orderItemState.getCad_status().equals("SUCCESS")) {
                                showDialog("CAD", "Customer has already received CAD message.");
                            } else {
                                processCADTimeSelection();
                            }
                        }

                    };
                    showDialog(actions4SimpleDlg);
                    return;
                }
                if (orderItemState.getCad_status().equals("SUCCESS")) {
                    showDialog("CAD", "Customer has already received CAD message.");
                } else {
                    processCADTimeSelection();
                }

                // actions4SimpleDlg.confirmListener = new OnClickListener() {
                // @Override
                // public void onClick(View v) {
                //
                // }
                //
                // private void performCAD() {
                // if (orderItemState.getCad_status().equals("SUCCESS")) {
                // showDialog("CAD", "Customer has already received CAD
                // message.");
                // } else {
                // processCADTimeSelection();
                // }
                // }
                //
                // };
                // performCAD（）
            }
        });
        ll_send_link.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLink();
            }
        });
        ll_messager.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toMessageView(localOrder);
            }
        });
    }

    @Override
    protected void onResume() {
        try {
            if (localOrder != null) {
                initView();
            }
        } catch (Exception e) {
            e.printStackTrace();
            SimpleToast.ToastError(e);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            dbHelperImpl.setDataToLocal(orderItemState.toString(), cacheKey);
        } catch (DBException e) {
            SimpleToast.ToastError(e);
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void toMessageView(CurrentOrderItem orderItem) {
        Intent intent = new Intent(this, MessagerActivity.class);
        intent.putExtra(CurrentOrderItem.class.getName(), orderItem);
        startActivity(intent);
    }

    @SuppressLint("InflateParams")
    private void processETATimeSelection() {
        Actions4SimpleDlg processETATimeDlg = new Actions4SimpleDlg();
        View view = getLayoutInflater().inflate(R.layout.layou_send_eta_dialog, null, false);
        processETATimeDlg.layoutView = view;
        final SimpleDialogFragment dialog = showDialog(processETATimeDlg);
        final EditText ed_eta = (EditText) view.findViewById(R.id.ed_eta);
        view.findViewById(R.id.btn_2).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                final String time = ed_eta.getText().toString();
                if (TextUtils.isEmpty(time)) {
                    return;
                }
                int timevalue = Integer.valueOf(time);
                if (!(timevalue >= 1 && timevalue <= 180)) {
                    showDialog("ETA", " input 1-180 mins.");
                    return;
                }
                DriverCommonControl control = new DriverCommonControl();
                ProgressCallback<BaseResponseBean> updateViewAsyncCallback = new ProgressCallback<BaseResponseBean>(
                        CurrentOrderDetialActivity.this) {
                    @Override
                    public void onPostExecute(BaseResponseBean arg0) {
                        if (arg0.getStatus()) {
                            orderItemState.setEta_status("SUCCESS");
                            showDialog("ETA", "ETA send success!");
                        }
                        super.onPostExecute(arg0);
                    }

                    @Override
                    public void onException(IException exception) {
                        super.onException(exception);
                        if (!(exception instanceof ServerException)) {
                            showActionFailWithAction("ETA", time);
                        }
                    }

                };
                control.sentETA(updateViewAsyncCallback, localOrder.getId(), time);
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

    private void processCOBTimeSelection() {
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<BaseResponseBean> updateViewAsyncCallback = new ProgressCallback<BaseResponseBean>(
                CurrentOrderDetialActivity.this) {
            @Override
            public void onPostExecute(BaseResponseBean arg0) {
                if (arg0.getStatus()) {
                    orderItemState.setCob_status("SUCCESS");
                    showDialog("COB", "COB send success!");
                }
                super.onPostExecute(arg0);
            }

            @Override
            public void onException(IException exception) {
                super.onException(exception);
                super.onException(exception);
                if (!(exception instanceof ServerException)) {
                    showActionFailWithAction("COB");
                    super.onException(null);
                } else {
                    super.onException(exception);
                }

            }
        };
        control.sentCOB(updateViewAsyncCallback, localOrder.getId());
    }

    private void processCOBExtension() {
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<BaseResponseBean> updateViewAsyncCallback = new ProgressCallback<BaseResponseBean>(
                CurrentOrderDetialActivity.this) {
            @Override
            public void onPostExecute(BaseResponseBean arg0) {
                if (arg0.getStatus()) {
                    showDialog("COB Extension", "COB Extension sent!");
                }
                super.onPostExecute(arg0);
            }

            @Override
            public void onException(IException exception) {
                super.onException(exception);
                if (!(exception instanceof ServerException)) {
                    showActionFailWithAction("COBEXT");
                    super.onException(null);
                } else {
                    super.onException(exception);
                }
            }
        };
        control.sentCOBExtension(updateViewAsyncCallback, localOrder.getId());
    }

    private void processCADTimeSelection() {
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<BaseResponseBean> updateViewAsyncCallback = new ProgressCallback<BaseResponseBean>(
                CurrentOrderDetialActivity.this) {
            @Override
            public void onPostExecute(BaseResponseBean arg0) {
                if (arg0.getStatus()) {
                    orderItemState.setCad_status("SUCCESS");
                    SimpleToast.ToastMessage("Customer has already received CAD message.");
                    setResult(RESULT_OK);
                    String receivable = localOrder.getReceivable();
                    if (receivable == null || receivable.equals("0.00")) {
                        super.onPostExecute(arg0);
                        return;
                    }
                    Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
                    actions4SimpleDlg.title = "Reminder";
                    actions4SimpleDlg.message = "Don't forget receivable: " + localOrder.getReceivable();
                    actions4SimpleDlg.cancelble = false;
                    actions4SimpleDlg.confirmListener = new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    };
                    showDialog(actions4SimpleDlg);
                }
                super.onPostExecute(arg0);
            }

            @Override
            public void onException(IException exception) {
                if (!(exception instanceof ServerException)) {
                    showActionFailWithAction("CAD");
                    super.onException(null);
                } else {
                    super.onException(exception);
                }
            }
        };
        control.sendCAD(updateViewAsyncCallback, localOrder.getId());
    }

    private void processArrivalSelection() {
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<BaseResponseBean> updateViewAsyncCallback = new ProgressCallback<BaseResponseBean>(
                CurrentOrderDetialActivity.this) {
            @Override
            public void onPostExecute(BaseResponseBean arg0) {
                if (arg0.getStatus()) {
                    orderItemState.setArrival_status("SUCCESS");
                    showDialog("ARRIVAL", "ARRIVAL send success!");
                }
                super.onPostExecute(arg0);
            }

            @Override
            public void onException(IException exception) {
                super.onException(exception);
                if (!(exception instanceof ServerException)) {
                    showActionFailWithAction("ARRIVAL");
                    super.onException(null);
                } else {
                    super.onException(exception);
                }

            }
        };
        control.sendArrival(updateViewAsyncCallback, localOrder.getId());
    }

    private void sendLink() {
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<BaseResponseBean> updateViewAsyncCallback = new ProgressCallback<BaseResponseBean>(
                CurrentOrderDetialActivity.this) {
            @Override
            public void onPostExecute(BaseResponseBean arg0) {
                if (arg0.getStatus()) {
                    showDialog("Customer Link", "Link sent!");
                }
                super.onPostExecute(arg0);
            }

            @Override
            public void onException(IException exception) {
                super.onException(exception);
            }
        };
        control.sendLink(updateViewAsyncCallback, localOrder.getId());
    }

    private void performGoogle(String location) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        // 从当前地点导航到Las Vegas
        Uri uri = Uri.parse("http://maps.google.com/maps?daddr=" + location);
        // 从Boston导航到Las Vegas
        // Uri uri = Uri.parse("http://maps.google.com/maps?daddr=Las Vegas");
        // 也可以这样用：导航到Las Vegas
        // Uri uri = Uri.parse("google.navigation:q=" + mResultQuery);
        intent.setData(uri);
        startActivity(intent);
    }

    /**
     * @param parms order_id,action,eta_time
     * @return
     */
    private String getSms(String... parms) {
        String sms = "#SunshireOrderAction#";
        if (parms != null) {
            for (int i = 0; i < parms.length; i++) {
                sms += "*" + parms[i];
            }
        }
        Loger.d("sms" + sms);
        return sms;
    }

    private void showActionFailWithAction(String action) {
        showActionFailWithAction(action, "");
    }

    private void showActionFailWithAction(String action, String etaTime) {
        OnClickListener okAction = null;
        String sms = null;
        String orderId = localOrder.getId();
        switch (action) {
            case "ETA":
                if (TextUtils.isEmpty(etaTime)) {
                    etaTime = "0";
                }
                sms = getSms(orderId, action, etaTime);
                break;
            case "ARRIVAL":
                sms = getSms(orderId, action);
                break;
            case "COBEXT":
                sms = getSms(orderId, action);
                break;
            case "COB":
                sms = getSms(orderId, action);
                break;
            case "CAD":
                sms = getSms(orderId, action);
                break;
            case "COBWARN":// XXX
                sms = getSms(orderId, action);
                break;
            default:
                break;
        }
        // NSString * backupPhone = @"18885018286";
        if (!TextUtils.isEmpty(sms)) {
            final String smsSend = sms;
            okAction = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String backupPhone = "18885018286";
                    Uri uri = Uri.parse("smsto:"
                            + backupPhone /* localOrder.getSystem_phone() */);
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra("sms_body", smsSend);
                    startActivity(intent);
                }
            };
            Actions4SimpleDlg actions4SimpleDlg = new Actions4SimpleDlg();
            actions4SimpleDlg.title = action;
            actions4SimpleDlg.message = "Network failed,Send " + action + " by SMS?";
            actions4SimpleDlg.cancelble = false;
            actions4SimpleDlg.confirmListener = okAction;
            showDialog(actions4SimpleDlg);
        }

    }

    private class CurrentOrderItemState extends BaseResponseBean {
        private static final long serialVersionUID = 1L;
        private static final String SUCCESS = "SUCCESS";
        private String eta_status = "";
        private String arrival_status = "";
        private String cob_status = "";
        private String cad_status = "";

        public CurrentOrderItemState() {
            eta_status = "RAW";
            arrival_status = "RAW";
            cob_status = "RAW";
            cad_status = "RAW";
        }

        @SuppressWarnings("deprecation")
        private void setViewState(View view, String State) {
            if (State.equals(SUCCESS)) {
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_green_xml));
                if (view instanceof TextView) {
                    try {
                        TextView title = (TextView) view;
                        if (!title.getText().toString().endsWith("SENT")) {
                            if ("ARRIVAL".equals(title.getText().toString())) {
                                title.setText("ARV" + " SENT");
                            } else {
                                title.setText(title.getText().toString() + " SENT");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_red_xml));
            }
        }

        public String getEta_status() {
            return eta_status;
        }

        public void setEta_status(String eta_status) {
            this.eta_status = eta_status;
            setViewState(ll_eta_sent, eta_status);
        }

        public String getArrival_status() {
            return arrival_status;
        }

        public void setArrival_status(String arrival_status) {
            this.arrival_status = arrival_status;
            setViewState(ll_arrival, arrival_status);
        }

        public String getCob_status() {
            return cob_status;
        }

        public void setCob_status(String cob_status) {
            this.cob_status = cob_status;
            setViewState(ll_cob, cob_status);
        }

        public String getCad_status() {
            return cad_status;
        }

        public void setCad_status(String cad_status) {
            this.cad_status = cad_status;
            setViewState(ll_cad, cad_status);
        }

        public void setData(CurrentOrderItemState orderItemStateData) {
            if (orderItemStateData == null) {
                return;
            }
            setEta_status(orderItemStateData.eta_status);
            setArrival_status(orderItemStateData.arrival_status);
            setCob_status(orderItemStateData.cob_status);
            setCad_status(orderItemStateData.cad_status);
        }

    }
}
