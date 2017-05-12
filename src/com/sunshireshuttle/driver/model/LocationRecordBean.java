package com.sunshireshuttle.driver.model;

import android.location.Location;

/**
 * @author Administrator
 *         位置记录
 */
public class LocationRecordBean extends BaseBean {

    private static final long serialVersionUID = 1L;


    private String tripId;
    private long recordTime;
    private String longitude;//经度
    private String latitude;//纬度
    private String action;//纬度
    /**
     * 记录状态
     * 0：未上传
     * 1：上传成功
     * 2：上传失败
     */
    private int state;
    /**
     * 0 send
     * 1 record
     */
    private int type;

    public LocationRecordBean() {
        // TODO Auto-generated constructor stub
    }

    public LocationRecordBean(Location location, String tripId, long recordTime, String action/*,int type*/) {
        this.tripId = tripId;
        this.recordTime = recordTime;
        longitude = ((Double) location.getLongitude()).toString();
        latitude = ((Double) location.getLatitude()).toString();
        state = 0;
        this.action = action;
//		this.type=type;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
