package com.sunshireshuttle.driver.model;

import java.util.ArrayList;

public class CurrentOrderResponse extends BaseResponseBean {
    private static final long serialVersionUID = 1L;
    private ArrayList<CurrentOrderItem> result;

    public ArrayList<CurrentOrderItem> getResult() {
        return result;
    }

    public void setResult(ArrayList<CurrentOrderItem> result) {
        this.result = result;
    }
}
