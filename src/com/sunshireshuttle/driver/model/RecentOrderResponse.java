package com.sunshireshuttle.driver.model;

import java.util.ArrayList;

public class RecentOrderResponse extends BaseResponseBean {
    private static final long serialVersionUID = 1L;
    private ArrayList<RecentOrderItem> result;

    public ArrayList<RecentOrderItem> getResult() {
        return result;
    }

    public void setResult(ArrayList<RecentOrderItem> result) {
        this.result = result;
    }
}
