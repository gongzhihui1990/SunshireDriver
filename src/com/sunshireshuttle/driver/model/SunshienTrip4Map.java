package com.sunshireshuttle.driver.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by caroline on 2016/11/27.
 */

public class SunshienTrip4Map extends BaseBean {
    private String tripId;
    private ArrayList<SunshineTripNode4Map> tripNodes;
    private SunshineTripNode4Map startPoint;
    private SunshineTripNode4Map endPoint;
    private Date startDate;
    private Date endDate;
    private String driverId;


    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public SunshineTripNode4Map getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(SunshineTripNode4Map endPoint) {
        this.endPoint = endPoint;
    }

    public ArrayList<SunshineTripNode4Map> getTripNodes() {
        return tripNodes;
    }

    public void setTripNodes(ArrayList<SunshineTripNode4Map> tripNodes) {
        this.tripNodes = tripNodes;
    }

    public SunshineTripNode4Map getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(SunshineTripNode4Map startPoint) {
        this.startPoint = startPoint;
    }
    //    @property NSString * tripId;
//    @property NSArray * tripNodes;
//    @property SunshireTripNode * startPoint;
//    @property SunshireTripNode * endPoint;
//    @property NSDate * startDate;
//    @property NSDate * endDate;
//    @property NSString * driverId;
}
