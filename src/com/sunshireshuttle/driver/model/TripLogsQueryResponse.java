package com.sunshireshuttle.driver.model;

import java.util.ArrayList;

public class TripLogsQueryResponse extends BaseResponseBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<TripLogBean> log_list;

    public ArrayList<TripLogBean> getLog_list() {
        return log_list;
    }

    public void setLog_list(ArrayList<TripLogBean> log_list) {
        this.log_list = log_list;
    }
}
