package com.sunshireshuttle.driver.model;

import java.util.ArrayList;

/**
 * Created by caroline on 2016/11/27.
 */

public class SunshineTripResponse extends BaseResponseBean {

    private ArrayList<SunshineTripNode> trip_nodes;
    private SunshineTripNode start_point;
    private SunshineTripNode end_point;

    public ArrayList<SunshineTripNode> getTrip_nodes() {
        return trip_nodes;
    }

    public void setTrip_nodes(ArrayList<SunshineTripNode> trip_nodes) {
        this.trip_nodes = trip_nodes;
    }

    public SunshineTripNode getStart_point() {
        return start_point;
    }

    public void setStart_point(SunshineTripNode start_point) {
        this.start_point = start_point;
    }

    public SunshineTripNode getEnd_point() {
        return end_point;
    }

    public void setEnd_point(SunshineTripNode end_point) {
        this.end_point = end_point;
    }
    //    "idtrip_history": "156",
//            "start_time": "2016-11-25 08:33:34",
//            "end_time": "2016-11-25 08:34:06",
//            "mile_age": "0",
//            "time_consumed": "0",
//            "trip_id": "2016112523333213",
//            "driver_id": "13",
//            "reg_date": "2016-11-25 08:34:07",
//            "is_original_time": "1",
//            "start_point": "1037006",
//            "end_point": "1037009"
}
