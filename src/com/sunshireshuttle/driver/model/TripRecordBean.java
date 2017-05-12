package com.sunshireshuttle.driver.model;

import java.text.NumberFormat;

/**
 * Created by caroline on 2016/11/26.
 */

public class TripRecordBean extends BaseBean {
    private String idtrip_history;
    private String start_time;
    private String end_time;
    private String mile_age;
    private String time_consumed;
    private String trip_id;
    private String driver_id;
    private String reg_date;
    private String is_original_time;
    private String start_point;
    private String end_point;

    public void setIdtrip_history(String idtrip_history) {
        this.idtrip_history = idtrip_history;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setMile_age(String mile_age) {
        this.mile_age = mile_age;
    }

    public void setTime_consumed(String time_consumed) {
        this.time_consumed = time_consumed;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public void setIs_original_time(String is_original_time) {
        this.is_original_time = is_original_time;
    }

    public void setStart_point(String start_point) {
        this.start_point = start_point;
    }

    public void setEnd_point(String end_point) {
        this.end_point = end_point;
    }

    public String getIdtrip_history() {
        return idtrip_history;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getMile_age() {
        return mile_age;
    }

    public String getMile_age_show() {
        try {
            Double mileAge = Double.valueOf(mile_age);
            NumberFormat format = NumberFormat.getInstance();
            format.setMaximumFractionDigits(2);
            return format.format(mileAge);
        } catch (Exception e) {
            e.printStackTrace();
            return mile_age;
        }
    }

    public String getTime_consumed() {
        return time_consumed;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public String getReg_date() {
        return reg_date;
    }

    public String getIs_original_time() {
        return is_original_time;
    }

    public String getStart_point() {
        return start_point;
    }

    public String getEnd_point() {
        return end_point;
    }


}
