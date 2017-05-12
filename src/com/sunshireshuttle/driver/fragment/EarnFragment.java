package com.sunshireshuttle.driver.fragment;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.bigkoo.pickerview.world.TimePickerView;
import com.sunshireshuttle.driver.BaseLazyFragment;
import com.sunshireshuttle.driver.R;
import com.sunshireshuttle.driver.SunDriverApplication;
import com.sunshireshuttle.driver.activity.SunshineTripDetialViewActivity;
import com.sunshireshuttle.driver.adapter.MyBaseAdapter;
import com.sunshireshuttle.driver.control.DriverCommonControl;
import com.sunshireshuttle.driver.control.ProgressCallback;
import com.sunshireshuttle.driver.model.TripRecordBean;
import com.sunshireshuttle.driver.model.TripRecordListResponseBean;
import com.sunshireshuttle.driver.utils.DateUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import net.iaf.framework.exception.IException;

public class EarnFragment extends BaseLazyFragment {
    TimePickerView pwTime;
    TextView tvFrom, tvTo, tvSearch, tvResult;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pwTime = new TimePickerView(getActivity(), TimePickerView.Type.YEAR_MONTH_DAY);
        // 时间选择后回调
        pwTime.setCyclic(false);
        pwTime.setCancelable(true);
        return inflater.inflate(R.layout.fragment_earing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        disableBackView();
        setTitle("Earn");
        listView = (ListView) findViewById(R.id.list);
        tvFrom = (TextView) findViewById(R.id.tv_from);
        tvTo = (TextView) findViewById(R.id.tv_to);
        tvSearch = (TextView) findViewById(R.id.tv_search);
        tvResult = (TextView) findViewById(R.id.tv_result);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date dateFrom = calendar.getTime();
        Date dateTo = new Date();
        tvFrom.setTag(dateFrom);
        tvFrom.setText(DateUtils.getDateDay(dateFrom));
        tvTo.setTag(dateTo);
        tvTo.setText(DateUtils.getDateDay(dateTo));
        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwTime.setTime((Date) tvFrom.getTag());
                pwTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                    @Override
                    public void onTimeSelect(Date date) {
                        tvFrom.setTag(date);
                        tvFrom.setText(DateUtils.getDateDay(date));
                    }
                });
                pwTime.show();
            }
        });
        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwTime.setTime((Date) tvTo.getTag());
                pwTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                    @Override
                    public void onTimeSelect(Date date) {
                        tvTo.setTag(date);
                        tvTo.setText(DateUtils.getDateDay(date));
                    }
                });
                pwTime.show();
            }
        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = tvFrom.getText().toString() + " 00:00";
                String end = tvTo.getText().toString() + " 23:59";
                queryTrips(start, end);
            }
        });
//        tvSearch.performClick();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onFirstUserVisible() {
        tvSearch.performClick();
        super.onFirstUserVisible();
    }

    private void queryTrips(String start, String end) {
        DriverCommonControl control = new DriverCommonControl();
        ProgressCallback<TripRecordListResponseBean> updateViewAsyncCallback = new ProgressCallback<TripRecordListResponseBean>(
                getBaseActivity()) {
            private final String cacheKey = "getHistoryOrderl";

            @Override
            public void onPostExecute(TripRecordListResponseBean response) {
                if (response != null && response.isResponseOK()) {
                    configResult(response);
                }
                super.onPostExecute(response);
            }

            @Override
            public void onException(IException exception) {
                super.onException(exception);
            }
        };
        String drivrId = SunDriverApplication.getInstance().dirverToken.getId();
        control.sendRequestForSearchTripHistoryWithConditions(updateViewAsyncCallback, start, end, drivrId);
    }


    private void configResult(TripRecordListResponseBean recordListResponseBean) {
        String time, money;
        double time_consumed = 0.0;
        ArrayList<TripRecordBean> list = recordListResponseBean.getTrip_list();
        for (TripRecordBean bean : list) {
            time_consumed += Double.valueOf(bean.getTime_consumed());
        }
        time_consumed = time_consumed / 60;
        double money_earned = time_consumed * 12;

        try {
            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(2);
            time = format.format(time_consumed);
            money = format.format(money_earned);
        } catch (Exception e) {
            e.printStackTrace();
            money = money_earned + "";
            time = time_consumed + "";
        }
        String resultStr = "Estimated earning: $" + money + "--- Time:" + time + "(hour)";
        tvResult.setText(resultStr);
        listView.setAdapter(new TripRecordsAdapter(list));
    }

    class TripRecordsAdapter extends MyBaseAdapter {
        ArrayList<TripRecordBean> list = new ArrayList<>();

        public TripRecordsAdapter(ArrayList<TripRecordBean> list) {
            this.list.clear();
            this.list.addAll(list);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public TripRecordBean getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_triprecord_list, parent, false);
                holder = new Holder();
                holder.tv1 = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv2 = (TextView) convertView.findViewById(R.id.tv_content);
                convertView.setTag(holder);
            }
            holder = (Holder) convertView.getTag();
            final TripRecordBean recordBean = getItem(position);
            String str1 = recordBean.getStart_time() + "---" + recordBean.getEnd_time();
            try {
                Date startDate = DateUtils.getDate(recordBean.getStart_time(), DateUtils.dateFormatLong);
                Date endDate = DateUtils.getDate(recordBean.getEnd_time(), DateUtils.dateFormatLong);
                String s1 = DateUtils.dateFormat4.format(startDate);
                String s2 = DateUtils.dateFormat4.format(endDate);
                str1 = s1 + "---" + s2;
            } catch (Exception e) {
                e.printStackTrace();
            }
            String str2 = "Mile age:" + recordBean.getMile_age_show() + " miles --- Time:" + recordBean.getTime_consumed() + " mins";
            holder.tv1.setText(str1);
            holder.tv2.setText(str2);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), SunshineTripDetialViewActivity.class);
                    intent.putExtra(TripRecordBean.class.getName(), recordBean);
                    getContext().startActivity(intent);
//                    ProgressCallback<BaseResponseBean> callback = new ProgressCallback<BaseResponseBean>(getBaseActivity()){
//                        @Override
//                        public void onPostExecute(BaseResponseBean arg0) {
//                            super.onPostExecute(arg0);
//                        }
//                    };
//                    new DriverCommonControl().sendRequestForSearchCompleteTripWithID(callback,recordBean.getTrip_id());
                }
            });
            return convertView;
        }

        class Holder {
            TextView tv1, tv2;
        }
    }
//    -(void) configResult{
//        double time_consumed = 0.0;
//        NSNumber * num_pool;
//        for (NSDictionary * dict in trip_list) {
//            num_pool = [CYFunctionSet convertStringToNumber:[dict objectForKey:@"time_consumed"]];
//            time_consumed += num_pool.doubleValue;
//        }
//        double money_earned = time_consumed /60 * 12;
//        NSString * temp = [NSString stringWithFormat:@"Estimated earning: $ %.2f --- Time: %.1f(hour)", money_earned,time_consumed/60];
//        [self setResultLabelText:temp];
//    }


}
