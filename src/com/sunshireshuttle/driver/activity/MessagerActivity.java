package com.sunshireshuttle.driver.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sunshireshuttle.driver.BaseActivity;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.adapter.MessageListAdapter;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.model.BaseResponseBean;
import com.sunshireshuttle.driver.model.CurrentOrderItem;
import com.sunshireshuttle.driver.model.MessageItem;
import com.sunshireshuttle.driver.model.MessageListResponse;

import net.iaf.framework.exception.IException;
import net.iaf.framework.util.EmptyUtil;

import java.util.ArrayList;

public class MessagerActivity extends BaseActivity {

    private String productId;
    private View reflashView;
    private View tvSend;
    private EditText etSend;
    private ListView lvMessage;
    private MessageListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);
        CurrentOrderItem orderItem = (CurrentOrderItem) getIntent().getSerializableExtra(CurrentOrderItem.class.getName());
        productId = orderItem.getId();

        TextView tvToolbarBack = (TextView) findViewById(R.id.tv_toolbar_back);
        tvToolbarBack.setText(R.string.back);
        tvToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.tv_toolbar_title);
        title.setText("Order #" + orderItem.getId());
        tvSend = findViewById(R.id.tv_send);
        etSend = (EditText) findViewById(R.id.et_send);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentSend = etSend.getText().toString();
                if (!EmptyUtil.isEmpty(contentSend)) {
                    //TODO send
                    sendMessageWithTripID(contentSend);
                }
            }
        });
        etSend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEND:
                        String contentSend = etSend.getText().toString();
                        if (!EmptyUtil.isEmpty(contentSend)) {
                            //TODO send
                            sendMessageWithTripID(contentSend);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        lvMessage = (ListView) findViewById(R.id.lv_message);
        reflashView = findViewById(R.id.reflash);
        reflashView.setVisibility(View.VISIBLE);
        reflashView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onRenderMessageList();
            }
        });
        onRenderMessageList();
    }

    private void sendMessageWithTripID(String message) {
        {
            DriverCommonControl control = new DriverCommonControl();
            ProgressCallback<BaseResponseBean> callback = new ProgressCallback<BaseResponseBean>(this) {
                @Override
                public void onPreExecute() {
                    reflashView.setEnabled(false);
                    super.onPreExecute();
                }

                @Override
                public void onException(IException exception) {
                    reflashView.setEnabled(true);
                    super.onException(exception);
                }

                @Override
                public void onPostExecute(BaseResponseBean arg0) {
                    reflashView.setEnabled(true);
                    etSend.setText("");
                    onRenderMessageList();
                    super.onPostExecute(arg0);
                }
            };
            control.sendMessageWithTripID(callback, productId, message);
        }
    }

    private void onRenderMessageList() {
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<MessageListResponse> callback = new ProgressCallback<MessageListResponse>(this) {
            @Override
            public void onPreExecute() {
                reflashView.setEnabled(false);
                super.onPreExecute();
            }

            @Override
            public void onException(IException exception) {
                reflashView.setEnabled(true);
                super.onException(exception);
            }

            @Override
            public void onPostExecute(MessageListResponse arg0) {
                reflashView.setEnabled(true);
                ArrayList<MessageItem> items = arg0.getMessage_list();
                listAdapter = new MessageListAdapter(items, MessagerActivity.this);
                lvMessage.setAdapter(listAdapter);
                super.onPostExecute(arg0);
            }
        };
        control.requestMessageWithTripID(callback, productId);
    }

//    -(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
//
//        dataPool = [message_list objectAtIndex:indexPath.row];
//        NSString * direction = [dataPool objectForKey:@"direction"];
//        if ((!direction) || [direction isEqualToString:@""]) {
//            return nil;
//        }
//        if ([direction isEqualToString:@"in"]) {
    //  Handle Customer
//            SunshireMessengerCustomerCellTableViewCell * cCell =  (SunshireMessengerCustomerCellTableViewCell * )[tableView dequeueReusableCellWithIdentifier:@"customerCell"];
//            cCell.userInteractionEnabled = YES;
//            cCell.cellImageView.image = [UIImage imageNamed:@"icon_customer"];
//            cCell.messageTextView.text = [dataPool objectForKey:@"message_content"];
//            cCell.messageTextView.userInteractionEnabled = YES;
//            cCell.messageTextView.editable = NO;
//            cCell.dateLabel.text =[dataPool objectForKey:@"reg_date"]; return cCell;
//        }
//
//        if ([direction isEqualToString:@"out"]) {
//            DispatchHandle Dispatch
//            SunshireMessengerDispatchCell * dCell = (SunshireMessengerDispatchCell *)[tableView dequeueReusableCellWithIdentifier:@"dispatchCell"];
//            dCell.userInteractionEnabled =YES;
//            dCell.cellImageView.image = [UIImage imageNamed:@"icon_manager"];
//            NSString * staff_type = [dataPool objectForKey:@"staff_type"];
//
//            if ([staff_type isEqualToString:@"DRIVER"]) {
//                dCell.cellImageView.image = [UIImage imageNamed:@"icon_driver"];
//            }
//            if ([staff_type isEqualToString:@"DISPATCH"]) {
//                dCell.cellImageView.image = [UIImage imageNamed:@"icon_dispatch"];
//            }
//
//            dCell.messageTextView.text = [dataPool objectForKey:@"message_content"];
//            dCell.messageTextView.userInteractionEnabled = YES;
//            dCell.messageTextView.editable = NO;
//            dCell.dateLabel.text =[dataPool objectForKey:@"reg_date"];
//            return dCell;
//        }
//
//        return nil;
//    }

}
