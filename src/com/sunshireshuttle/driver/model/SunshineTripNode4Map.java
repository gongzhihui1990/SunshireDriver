package com.sunshireshuttle.driver.model;

import android.location.Location;

import com.sunshireshuttle.driver.utils.DateUtils;

import net.iaf.framework.util.Loger;

import java.util.Date;

/**
 * Created by caroline on 2016/11/27.
 */

public class SunshineTripNode4Map extends BaseBean {
    //    {"0":"156","idtrip_history":"156","1":"2016-11-25 08:33:34","start_time":"2016-11-25 08:33:34","2":"2016-11-25 08:34:06","end_time":"2016-11-25 08:34:06","3":"0","mile_age":"0","4":"0","time_consumed":"0","5":"2016112523333213","trip_id":"2016112523333213","6":"13","driver_id":"13","7":"2016-11-25 08:34:07","reg_date":"2016-11-25 08:34:07","8":"1","is_original_time":"1","9":"1037006","start_point":"1037006","10":"1037009","end_point":"1037009"}
    private double latitude;
    private double longitude;
    private Date time_stamp;
    private String action;

    public SunshineTripNode4Map(SunshineTripNode node) {
        this.latitude = node.getLatitude();
        this.longitude = node.getLongitude();
        this.action = node.getAction();
        time_stamp = DateUtils.parseDate(node.getReg_date(), DateUtils.dateFormatLong);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

//
//    +(double) calculateKPHSpeedWithCurrentNode:(SunshireTripNode *) currentNode
//    forLastNode:(SunshireTripNode *) lastNode{
//        double lat1 = lastNode.latitude.doubleValue;
//        double lon1 = lastNode.longitude.doubleValue;
//
//        double lat2 = currentNode.latitude.doubleValue;
//        double lon2 = currentNode.longitude.doubleValue;
//
//        lat1 = lat1 * M_PI / 180.0;
//        lon1 = lon1 * M_PI / 180.0;
//
//        lat2 = lat2 * M_PI / 180.0;
//        lon2 = lon2 * M_PI / 180.0;
//
//        // radius of earth in metres
//        double r = 6378100;
//
//        double rho1 = r * cos(lat1);
//        double z1 = r * sin(lat1);
//        double x1 = rho1 * cos(lon1);
//        double y1 = rho1 * sin(lon1);
//
//        // Q
//        double rho2 = r * cos(lat2);
//        double z2 = r * sin(lat2);
//        double x2 = rho2 * cos(lon2);
//        double y2 = rho2 * sin(lon2);
//
//        // Dot product
//        double dot = (x1 * x2 + y1 * y2 + z1 * z2);
//        double cos_theta = dot / (r * r);
//
//        double theta = acos(cos_theta);
//        // Distance in Metres
//        double dis_in_me = r* theta;
//
//        double speed_mps = dis_in_me/[CYFunctionSet timeintervalFrom:lastNode.time_stamp to:currentNode.time_stamp];
//        double speed_kph = (speed_mps * 3600.0) / 1000.0;
//
//        return speed_kph;
//    }

    public static double calculateMPHSpeedWithCurrentNode(SunshineTripNode4Map currentNode, SunshineTripNode4Map lastNode) {
        double lat1 = lastNode.getLatitude();
        double lon1 = lastNode.getLongitude();

        double lat2 = currentNode.getLatitude();
        double lon2 = currentNode.getLongitude();

        double dis_in_me = getDistance(lat1, lon1, lat2, lon2);
//        lat1 = lat1 * Math.PI / 180.0;
//        ／lon1 = lon1 * Math.PI / 180.0;
//
//／        lat2 = lat2 * Math.PI / 180.0;
//    ／    lon2 = lon2 * Math.PI / 180.0;
//／
//    ／    // radius of earth in metres
//        ／double r = 6378100;
//／
//    ／    double rho1 = r * Math.cos(lat1);
//        ／double z1 = r * Math.sin(lat1);
//        double x1 = rho1 * Math.cos(lon1);
//        double／ y1 = rho1 * Math.sin(lon1);
//／
//    ／    // Q
//        double rho2 = r * Math.cos(lat2);
//        do／uble z2 = r * Math.sin(lat2);
//        double／ x2 = rho2 * Math.cos(lon2);
//        double y2 ／= rho2 * Math.sin(lon2);
//／
//    ／    // Dot product
//        ／double dot = (x1 * x2 + y1 * y2 + z1 * z2);
//        doub／le cos_theta = dot / (r * r);
//／
//    ／    double theta = Math.acos(cos_theta);
// Distance in Metres


        double speed_mps = dis_in_me / ((currentNode.getTime_stamp().getTime() - lastNode.getTime_stamp().getTime()) / 1000);
        double speed_kph = (speed_mps * 3600.0) / 1000.0;

        return speed_kph / 1/*1.60934*/;
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }
}
