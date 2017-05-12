package com.sunshireshuttle.driver.model;

import android.location.Location;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.sunshireshuttle.driver.MySPEdit;
import com.sunshireshuttle.driver.SunDriverApplication;

import java.io.Serializable;

public class DirverToken implements Serializable {

    private static final long serialVersionUID = 5822321375619318714L;
    transient public Location location;//自定义
    private int State = 0;//自定义 工作状态 0:off,1:on
    private String id;
    private String phone;
    private String name;
    private String tripId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
        MySPEdit.getInstance(SunDriverApplication.getInstance()).setDirverToken(this);
    }

    public String getTripId() {
        if (TextUtils.isEmpty(tripId)) {
            return "no tripId";
        }
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }


}
