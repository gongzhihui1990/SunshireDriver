package com.sunshireshuttle.driver.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunshireshuttle.driver.BaseLazyFragment;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.SunDriverApplication;
import com.sunshireshuttle.driver.activity.CurrentOrderDetialActivity;
import com.sunshireshuttle.driver.adapter.MyBaseAdapter;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.dao.DBHelperImpl;
import com.sunshireshuttle.driver.model.CurrentOrderItem;
import com.sunshireshuttle.driver.model.CurrentOrderResponse;

import net.iaf.framework.exception.DBException;
import net.iaf.framework.exception.IException;
import net.iaf.framework.util.EmptyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class CurrentOrdersFragment extends BaseLazyFragment {
    private ListView listView;
    private DBHelperImpl dbHelperImpl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelperImpl = new DBHelperImpl();
        return inflater.inflate(R.layout.fragment_current, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        disableBackView();
        setTitle("Current Orders");
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrentOrderItem item = (CurrentOrderItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), CurrentOrderDetialActivity.class);
                intent.putExtra(CurrentOrderItem.class.getName(), item);
                startActivityForResult(intent, 11);
            }
        });
        View reflashView = view.findViewById(R.id.reflash);
        reflashView.setVisibility(View.VISIBLE);
        reflashView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getCurrentOrderList();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void getCurrentOrderList() {
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<CurrentOrderResponse> updateViewAsyncCallback = new ProgressCallback<CurrentOrderResponse>(
                getBaseActivity()) {
            private final String cacheKey = "getCurrentOrderList";

            @Override
            public void onPostExecute(CurrentOrderResponse response) {
                super.onPostExecute(response);
                try {
                    dbHelperImpl.setDataToLocal(response, cacheKey);
                } catch (DBException e) {
                    e.printStackTrace();
                }
                setList(response.getResult());
                if (!EmptyUtil.isEmpty(response.getMessage())
                        && response.getMessage().equals("no result found")) {
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_LONG).show();
//					getBaseActivity().showDialog(null, response.getMessage());
//					setTitle("No Result");
                } else {
//					setTitle("Current Orders");
                }
            }

            private void setList(ArrayList<CurrentOrderItem> result) {
//				ArrayList<CurrentOrderItem> result = response.getResult();
                if (result != null && !result.isEmpty()) {
                    for (CurrentOrderItem orderItem : result) {
                        getBaseActivity().addNotificationsForOrder(orderItem);
                    }
                }
                CurrentOrderAdapter adapter = new CurrentOrderAdapter(result);
                listView.setAdapter(adapter);
            }

            private void setList(CurrentOrderResponse response) {
                ArrayList<CurrentOrderItem> result = response.getResult();
                setList(result);
            }

            @Override
            public void onException(IException exception) {
                try {
                    CurrentOrderResponse response = (CurrentOrderResponse) dbHelperImpl.getDataFromLocal(cacheKey);
                    setList(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.onException(exception);
            }

        };
        control.getCurrentOrderList(updateViewAsyncCallback, SunDriverApplication.getInstance().dirverToken.getId());
    }

    @Override
    public void onFirstUserVisible() {
        getCurrentOrderList();
        super.onFirstUserVisible();
    }

    public void receiveData(Intent data) {
        int alertType = data.getIntExtra("alertType", -1);
        CurrentOrderItem orderItem = (CurrentOrderItem) data.getSerializableExtra(CurrentOrderItem.class.getName());
        Intent intent = new Intent(getContext(), CurrentOrderDetialActivity.class);
        intent.putExtra(CurrentOrderItem.class.getName(), orderItem);
        intent.putExtra("showDialog", true);
        intent.putExtra("alertType", alertType);
        startActivityForResult(intent, 11);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 11:
                if (resultCode == Activity.RESULT_OK) {
                    getCurrentOrderList();
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class CurrentOrderAdapter extends MyBaseAdapter {
        ArrayList<CurrentOrderItem> items = new ArrayList<>();

        public CurrentOrderAdapter(ArrayList<CurrentOrderItem> items) {
            if (items != null) {
                Collections.sort(items);
                this.items = items;
            } else {
                this.items = new ArrayList<>();
            }
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public CurrentOrderItem getItem(int position) {
            return items.get(position);
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CurrentOrderItem item = getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_current_order, parent,
                        false);
                holder = new ViewHolder();
                holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
                holder.tv_pickup_time = (TextView) convertView.findViewById(R.id.tv_pickup_time);
                holder.tv_service_type = (TextView) convertView.findViewById(R.id.tv_service_type);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.tv_id.setText(item.getId());
            // 2016-08-05 23:27:00
            String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
            String TARGET_TIME_FORMAT = "MM/dd HH:mm";
            String picktime = item.getPickup_time();
            try {
                Date date = new SimpleDateFormat(DEFAULT_TIME_FORMAT).parse(picktime);
                picktime = new SimpleDateFormat(TARGET_TIME_FORMAT).format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.tv_pickup_time.setText(picktime);
            holder.tv_service_type.setText(item.getService_type());
            return convertView;
        }

        class ViewHolder {
            TextView tv_id, tv_pickup_time, tv_service_type;
        }
    }
}
