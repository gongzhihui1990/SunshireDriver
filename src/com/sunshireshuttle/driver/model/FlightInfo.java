package com.sunshireshuttle.driver.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;

import com.sunshireshuttle.driver.utils.DateUtils;

@SuppressLint("SimpleDateFormat")
public class FlightInfo extends BaseBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * "id": "52",
     */
    private String id;
    /**
     * "flight_id": "766102809",
     */
    private String flight_id;
    /**
     * "reg_date": "2016-08-26 03:30:02",
     */
    private String reg_date;
    /**
     * "carrier_code": "CP",
     */
    private String carrier_code;
    /**
     * "flight_num": "6057",
     */
    private String flight_num;
    /**
     * "dep_port": "LAX",
     */
    private String dep_port;
    /**
     * "arr_port": "SMF",
     */
    private String arr_port;
    /**
     * "dep_terminal": "6",
     */
    private String dep_terminal;
    /**
     * "arr_terminal": "A",
     */
    private String arr_terminal;
    /**
     * "arr_gate": "N/A",
     */
    private String arr_gate;
    /**
     * "baggage_claim": "N/A",
     */
    private String baggage_claim;
    /**
     * "dep_gate": "60D",
     */
    private String dep_gate;
    /**
     * "flight_status": "S",
     */
    private String flight_status;
    /**
     * "last_warning_arr": "2016-08-26 06:57:00",
     */
    private String last_warning_arr;
    /**
     * "last_warning_dep": "2016-08-26 05:30:00"
     */
    private String last_warning_dep;
    private String utcTimePatten = "yyyy-MM-dd HH:mm:SS";
    private String localTimePatten = "yyyy-MM-dd HH:mm";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(String flight_id) {
        this.flight_id = flight_id;
    }

    public String getReg_date() {
        try {
            return DateUtils.formatDateStr(reg_date, new SimpleDateFormat(utcTimePatten), new SimpleDateFormat(localTimePatten));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reg_date;

//        return UtilForTime.utc2Local(reg_date, utcTimePatten, localTimePatten);
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getCarrier_code() {
        return carrier_code;
    }

    public void setCarrier_code(String carrier_code) {
        this.carrier_code = carrier_code;
    }

    public String getFlight_num() {
        return flight_num;
    }

    public void setFlight_num(String flight_num) {
        this.flight_num = flight_num;
    }

    public String getDep_port() {
        return dep_port;
    }

    public void setDep_port(String dep_port) {
        this.dep_port = dep_port;
    }

    public String getArr_port() {
        return arr_port;
    }

    public void setArr_port(String arr_port) {
        this.arr_port = arr_port;
    }

    public String getDep_terminal() {
        return dep_terminal;
    }

    public void setDep_terminal(String dep_terminal) {
        this.dep_terminal = dep_terminal;
    }

    public String getArr_terminal() {
        return arr_terminal;
    }

    public void setArr_terminal(String arr_terminal) {
        this.arr_terminal = arr_terminal;
    }

    public String getArr_gate() {
        return arr_gate;
    }

    public void setArr_gate(String arr_gate) {
        this.arr_gate = arr_gate;
    }

    public String getBaggage_claim() {
        return baggage_claim;
    }

    public void setBaggage_claim(String baggage_claim) {
        this.baggage_claim = baggage_claim;
    }

    public String getDep_gate() {
        return dep_gate;
    }

    public void setDep_gate(String dep_gate) {
        this.dep_gate = dep_gate;
    }

    public String getFlight_status() {
        return flight_status;
    }

    public void setFlight_status(String flight_status) {
        this.flight_status = flight_status;
    }

    public String getLast_warning_arr() {
        try {
            return DateUtils.formatDateStr(last_warning_arr, new SimpleDateFormat(utcTimePatten), new SimpleDateFormat(localTimePatten));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        return UtilForTime.utc2Local(last_warning_arr, utcTimePatten, localTimePatten);
        return last_warning_arr;
    }

    public void setLast_warning_arr(String last_warning_arr) {
        this.last_warning_arr = last_warning_arr;
    }

    public String getLast_warning_dep() {
        try {
            return DateUtils.formatDateStr(last_warning_dep, new SimpleDateFormat(utcTimePatten), new SimpleDateFormat(localTimePatten));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        return UtilForTime.utc2Local(last_warning_dep, utcTimePatten, localTimePatten);
        return last_warning_dep;
    }

    public void setLast_warning_dep(String last_warning_dep) {
        this.last_warning_dep = last_warning_dep;
    }


}
