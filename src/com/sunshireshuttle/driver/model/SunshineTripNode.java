package com.sunshireshuttle.driver.model;

import java.util.Date;

/**
 * Created by caroline on 2016/11/27.
 */

public class SunshineTripNode extends BaseBean {
    //    "id": "991574",
//            "trip_id": "2016111522545913",
//            "latitude": "31.526014",
//            "longitude": "120.302065",
//            "reg_date": "2016-11-15 07:55:03",
//            "driver_code": "13",
//            "action": "1"
    private String id;
    private String trip_id;
    private double latitude;
    private double longitude;
    private String reg_date;
    private String driver_code;
    private String action;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getDriver_code() {
        return driver_code;
    }

    public void setDriver_code(String driver_code) {
        this.driver_code = driver_code;
    }
}
