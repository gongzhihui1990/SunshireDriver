package com.sunshireshuttle.driver.model;

import java.util.ArrayList;

/**
 * Created by caroline on 2016/11/26.
 */

public class TripRecordListResponseBean extends BaseResponseBean {
    private ArrayList<TripRecordBean> trip_list;

    public void setTrip_list(ArrayList<TripRecordBean> trip_list) {
        this.trip_list = trip_list;
    }

    public ArrayList<TripRecordBean> getTrip_list() {
        return trip_list;
    }
}
