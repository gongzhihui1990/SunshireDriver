package com.sunshireshuttle.driver.entity.jsonentity;

import com.sunshireshuttle.driver.constans.UrlConst;
import com.sunshireshuttle.driver.dao.BaseJsonEntity;
import com.sunshireshuttle.driver.model.CurrentOrderItem;

import java.util.ArrayList;

public class CurrentOrderList extends BaseJsonEntity<CurrentOrderList> {

    private static final long serialVersionUID = 1L;

    private ArrayList<CurrentOrderItem> result;

    public ArrayList<CurrentOrderItem> getResult() {
        return result;
    }

    public void setResult(ArrayList<CurrentOrderItem> result) {
        this.result = result;
    }

    @Override
    public String getUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getCacheTime() {
        // TODO Auto-generated method stub
        return UrlConst.CACHE_TIME_ONE_DAY;
    }

}
