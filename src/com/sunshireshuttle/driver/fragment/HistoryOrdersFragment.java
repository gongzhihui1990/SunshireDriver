package com.sunshireshuttle.driver.fragment;

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

import com.sunshireshuttle.driver.BaseLazyFragment;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.SunDriverApplication;
import com.sunshireshuttle.driver.activity.HistoryOrderDetialActivity;
import com.sunshireshuttle.driver.adapter.MyBaseAdapter;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.dao.DBHelperImpl;
import com.sunshireshuttle.driver.model.RecentOrderItem;
import com.sunshireshuttle.driver.model.RecentOrderResponse;
import com.sunshireshuttle.driver.utils.UtilForMoney;

import net.iaf.framework.exception.DBException;
import net.iaf.framework.exception.IException;

import java.util.ArrayList;

public class HistoryOrdersFragment extends BaseLazyFragment {

    private ListView listView;
    private TextView tv_owe;
    private DBHelperImpl dbHelperImpl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelperImpl = new DBHelperImpl();
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        disableBackView();
        setTitle("Recent Orders");
        View reflashView = view.findViewById(R.id.reflash);
        reflashView.setVisibility(View.VISIBLE);
        reflashView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getHistoryOrderl();
            }
        });
        tv_owe = (TextView) view.findViewById(R.id.tv_owe);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecentOrderItem item = (RecentOrderItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), HistoryOrderDetialActivity.class);
                intent.putExtra(RecentOrderItem.class.getName(), item);
                startActivity(intent);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onFirstUserVisible() {
        getHistoryOrderl();
        super.onFirstUserVisible();
    }

    private void getHistoryOrderl() {
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<RecentOrderResponse> updateViewAsyncCallback = new ProgressCallback<RecentOrderResponse>(
                getBaseActivity()) {
            private final String cacheKey = "getHistoryOrderl";

            @Override
            public void onPostExecute(RecentOrderResponse response) {
                super.onPostExecute(response);
                try {
                    dbHelperImpl.setDataToLocal(response, cacheKey);
                } catch (DBException e) {
                    e.printStackTrace();
                }
                setList(response);

            }

            private void setList(RecentOrderResponse response) {
                ArrayList<RecentOrderItem> result = response.getResult();
                long feng = 0;
                for (RecentOrderItem item : result) {
                    String money = item.getReceivable();
                    String fengItem = UtilForMoney.yuan2fen(money);
                    feng += Long.valueOf(fengItem);
                }
                if (feng > 0) {
                    tv_owe.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                } else {
                    tv_owe.setTextColor(getResources().getColor(R.color.gray));
                }
                String money = UtilForMoney.fen2yuan(feng);
                tv_owe.setText("$" + money);

                RecentOrderAdapter adapter = new RecentOrderAdapter(result);
                listView.setAdapter(adapter);
            }

            @Override
            public void onException(IException exception) {
                try {
                    RecentOrderResponse response = (RecentOrderResponse) dbHelperImpl.getDataFromLocal(cacheKey);
                    setList(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.onException(exception);
            }
        };
        control.getHistoryOrder(updateViewAsyncCallback, SunDriverApplication.getInstance().dirverToken.getId());
    }

    private class RecentOrderAdapter extends MyBaseAdapter {
        ArrayList<RecentOrderItem> items = new ArrayList<>();

        private RecentOrderAdapter(ArrayList<RecentOrderItem> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public RecentOrderItem getItem(int position) {
            return items.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RecentOrderItem item = getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).
                        inflate(R.layout.layout_item_recent_order, parent, false);
                holder = new ViewHolder();
                holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
                holder.tv_pickup_time = (TextView) convertView.findViewById(R.id.tv_pickup_time);
                holder.tv_customer_name = (TextView) convertView.findViewById(R.id.tv_customer_name);
                holder.tv_receivable = (TextView) convertView.findViewById(R.id.tv_receivable);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            holder.tv_id.setText(item.getId());
            holder.tv_pickup_time.setText(item.getPickup_time());
            holder.tv_customer_name.setText(item.getCustomer_name());
            if (item.getReceivable() != null && !item.getReceivable().equals("0.00")) {
                holder.tv_receivable.setTextColor(getResources().getColor(android.R.color.holo_red_light));

            } else {
                holder.tv_receivable.setTextColor(getResources().getColor(R.color.white_80));
            }
            holder.tv_receivable.setText("$" + item.getReceivable());
            return convertView;
        }

        class ViewHolder {
            TextView tv_id, tv_pickup_time,
                    tv_customer_name, tv_receivable;
        }
    }
}
