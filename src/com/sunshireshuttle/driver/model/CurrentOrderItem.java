package com.sunshireshuttle.driver.model;

import android.text.TextUtils;

import com.sunshireshuttle.driver.utils.UtilForTime;

import java.io.Serializable;

/**
 * @author Heisenberg heisenberg.gong@koolpos.com>
 * @ClassName: RecentOrder
 * @Description: 订单简要项
 * @date 2016年8月6日 下午9:15:05
 */

public class CurrentOrderItem implements Serializable, Comparable<CurrentOrderItem> {
    public static final int AlertEta = 1;
    public static final int AlertCob = 2;
    //    "system_phone": "18885017243"
    private static final long serialVersionUID = 1L;
    private String id;
    //	"id": "1916",
    private String order_id;
    //    "order_id": "1527",
    private String status;
    //    "status": "2",
    private String total;
    //    "total": "0.00",
    private String receivable;
    //    "receivable": "0.00",
    private String is_handed;
    //    "is_handed": "0",
    private String passenger;
    //    "passenger": "1",
    private String baggage;
    //    "baggage": "0",
    private String service_type;
    //    "service_type": "EXCLUSIVE",
    private String from_to_airport;
    //    "from_to_airport": "0",
    private String flight_type;
    //    "flight_type": "domestic",
    private String flight_num;
    //    "flight_num": "aaa 321",
    private String flight_datetime;
    //    "flight_datetime": "2016-08-25 01:00:00",
    private String flight_arrival_datetime;
    //    "flight_arrival_datetime": "0000-00-00 00:00:00",
    private String pickup_time;
    //    "pickup_time": "2016-08-07 23:27:00",
    private String pickup_time_to;
    //    "pickup_time_to": "2016-08-07 23:27:00",
    private String location_from;
    //    "location_from": "3629 lynoak dr, pomona CA 91767",
    private String arrival_time;
    //    "arrival_time": "0000-00-00 00:00:00",
    private String location_to;
    //    "location_to": "LAX",
    private String dispatch_datetime;
    //    "dispatch_datetime": "2016-08-06 17:08:03",
    private String eta_alert;
    //    "eta_alert": "0000-00-00 00:00:00",
    private String eta_alert_status;
    //    "eta_alert_status": "0",
    private String eta_datetime;
    //    "eta_datetime": "0000-00-00 00:00:00",
    private String arrival_alert;
    //    "arrival_alert": "2016-08-07 23:27:00",
    private String arrival_datetime;
    //    "arrival_datetime": "0000-00-00 00:00:00",
    private String cob_alert;
    //    "cob_alert": "0000-00-00 00:00:00",
    private String cob_alert_status;
    //    "cob_alert_status": "0",
    private String cob_ext_num;
    //    "cob_ext_num": "0",
    private String cob_datetime;
    //    "cob_datetime": "0000-00-00 00:00:00",
    private String cad_datetime;
    //    "cad_datetime": "0000-00-00 00:00:00",
    private String cob_customer_alert;
    //    "cob_customer_alert": "0",
    private String cad_customer_alert;
    //    "cad_customer_alert": "0",
    private String alert_phone;
    //    "alert_phone": "6268696682",
    private String alert_phoneb;
    //    "alert_phoneb": "6263437533",
    private String driver_id;
    //    "driver_id": "13",
    private String vehicle_id;
    //    "vehicle_id": "0",
    private String note;
    //    "note": "",
    private String share_driver_location;
    //    "share_driver_location": "1",
    private String ctoken;
    //    "ctoken": null,
    private String ctoken_cdate;
    //    "ctoken_cdate": null,
    private String pickup_status;
    //    "pickup_status": "0",
    private String customer_rating;
    //    "customer_rating": "-1",
    private String alert_status;
    //    "alert_status": "0",
    private String customer_name;
    private String flight_info_id;
    //    "customer_name": "chris yao",
    private String is_text_ok;
    //    "is_text_ok": "1",
    private String customer_phone;
    //    "customer_phone": "8145664700",
    private String is_text_okb;
    //    "is_text_okb": "1",
    private String customer_nameb;
    //    "customer_nameb": "chris yao",
    private String customer_phoneb;
    //    "customer_phoneb": "8145664700",
    private String driver_name;
    //    "driver_name": "Chris Test",
    private String system_phone;
    private String laTimePatten = "yyyy-MM-dd HH:mm:SS";
    private String localTimePatten = "yyyy-MM-dd HH:mm:SS";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getReceivable() {
        return receivable;
    }

    public void setReceivable(String receivable) {
        this.receivable = receivable;
    }

    public String getIs_handed() {
        return is_handed;
    }

    public void setIs_handed(String is_handed) {
        this.is_handed = is_handed;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getBaggage() {
        return baggage;
    }

    public void setBaggage(String baggage) {
        this.baggage = baggage;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getFrom_to_airport() {
        return from_to_airport;
    }

    public void setFrom_to_airport(String from_to_airport) {
        this.from_to_airport = from_to_airport;
    }

    public String getFlight_type() {
        return flight_type;
    }

    public void setFlight_type(String flight_type) {
        this.flight_type = flight_type;
    }

    public String getFlight_num() {
        return flight_num;
    }

    public void setFlight_num(String flight_num) {
        this.flight_num = flight_num;
    }

    public String getFlight_datetime() {
        return flight_datetime;
    }

    public void setFlight_datetime(String flight_datetime) {
        this.flight_datetime = flight_datetime;
    }

    public String getFlight_arrival_datetime() {
        return flight_arrival_datetime;
    }

    public void setFlight_arrival_datetime(String flight_arrival_datetime) {
        this.flight_arrival_datetime = flight_arrival_datetime;
    }

    public String getPickup_time() {
        return UtilForTime.la2Local(pickup_time, laTimePatten, localTimePatten);
//		return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getPickup_time_to() {
        return pickup_time_to;
    }

    public void setPickup_time_to(String pickup_time_to) {
        this.pickup_time_to = pickup_time_to;
    }

    public String getLocation_from() {
        return location_from;
    }

    public void setLocation_from(String location_from) {
        this.location_from = location_from;
    }

    public String getArrival_time() {
        return UtilForTime.la2Local(arrival_time, laTimePatten, localTimePatten);
//		return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getLocation_to() {
        return location_to;
    }

    public void setLocation_to(String location_to) {
        this.location_to = location_to;
    }

    public String getDispatch_datetime() {
        return dispatch_datetime;
    }

    public void setDispatch_datetime(String dispatch_datetime) {
        this.dispatch_datetime = dispatch_datetime;
    }

    public String getEta_alert() {
        return eta_alert;
    }

    public void setEta_alert(String eta_alert) {
        this.eta_alert = eta_alert;
    }

    public String getEta_alert_status() {
        return eta_alert_status;
    }

    public void setEta_alert_status(String eta_alert_status) {
        this.eta_alert_status = eta_alert_status;
    }

    public String getEta_datetime() {
        return eta_datetime;
    }

    public void setEta_datetime(String eta_datetime) {
        this.eta_datetime = eta_datetime;
    }

    public String getArrival_alert() {
        return arrival_alert;
    }

    public void setArrival_alert(String arrival_alert) {
        this.arrival_alert = arrival_alert;
    }

    public String getArrival_datetime() {
        return arrival_datetime;
    }

    public void setArrival_datetime(String arrival_datetime) {
        this.arrival_datetime = arrival_datetime;
    }

    public String getCob_alert() {
        return cob_alert;
    }

    public void setCob_alert(String cob_alert) {
        this.cob_alert = cob_alert;
    }

    public String getCob_alert_status() {
        return cob_alert_status;
    }

    public void setCob_alert_status(String cob_alert_status) {
        this.cob_alert_status = cob_alert_status;
    }

    public String getCob_ext_num() {
        return cob_ext_num;
    }

    public void setCob_ext_num(String cob_ext_num) {
        this.cob_ext_num = cob_ext_num;
    }

    public String getCob_datetime() {
        return cob_datetime;
    }

    public void setCob_datetime(String cob_datetime) {
        this.cob_datetime = cob_datetime;
    }

    public String getCad_datetime() {
        return cad_datetime;
    }

    public void setCad_datetime(String cad_datetime) {
        this.cad_datetime = cad_datetime;
    }

    public String getCob_customer_alert() {
        return cob_customer_alert;
    }

    public void setCob_customer_alert(String cob_customer_alert) {
        this.cob_customer_alert = cob_customer_alert;
    }

    public String getCad_customer_alert() {
        return cad_customer_alert;
    }

    public void setCad_customer_alert(String cad_customer_alert) {
        this.cad_customer_alert = cad_customer_alert;
    }

    public String getAlert_phone() {
        return alert_phone;
    }

    public void setAlert_phone(String alert_phone) {
        this.alert_phone = alert_phone;
    }

    /**
     * @return 经理的号码
     */
    public String getAlert_phoneb() {
        return alert_phoneb;
    }

    public void setAlert_phoneb(String alert_phoneb) {
        this.alert_phoneb = alert_phoneb;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getShare_driver_location() {
        return share_driver_location;
    }

    public void setShare_driver_location(String share_driver_location) {
        this.share_driver_location = share_driver_location;
    }

    public String getCtoken() {
        return ctoken;
    }

    public void setCtoken(String ctoken) {
        this.ctoken = ctoken;
    }

    public String getCtoken_cdate() {
        return ctoken_cdate;
    }

    public void setCtoken_cdate(String ctoken_cdate) {
        this.ctoken_cdate = ctoken_cdate;
    }

    public String getPickup_status() {
        return pickup_status;
    }

    public void setPickup_status(String pickup_status) {
        this.pickup_status = pickup_status;
    }

    public String getCustomer_rating() {
        return customer_rating;
    }

    public void setCustomer_rating(String customer_rating) {
        this.customer_rating = customer_rating;
    }

    public String getAlert_status() {
        return alert_status;
    }

    public void setAlert_status(String alert_status) {
        this.alert_status = alert_status;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getIs_text_ok() {
        return is_text_ok;
    }

    public void setIs_text_ok(String is_text_ok) {
        this.is_text_ok = is_text_ok;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getIs_text_okb() {
        return is_text_okb;
    }

    public void setIs_text_okb(String is_text_okb) {
        this.is_text_okb = is_text_okb;
    }

    public String getCustomer_nameb() {
        return customer_nameb;
    }

    public void setCustomer_nameb(String customer_nameb) {
        this.customer_nameb = customer_nameb;
    }

    public String getCustomer_phoneb() {
        return customer_phoneb;
    }

    public void setCustomer_phoneb(String customer_phoneb) {
        this.customer_phoneb = customer_phoneb;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getSystem_phone() {
        if (TextUtils.isEmpty(system_phone) || system_phone.equals("NOT AVAILIBLE")) {
            system_phone = "16268696682";
        }
        return system_phone;
    }

    public void setSystem_phone(String system_phone) {
        this.system_phone = system_phone;
    }

    @Override
    public int compareTo(CurrentOrderItem another) {
        return pickup_time.compareTo(another.getPickup_time());
    }

    public String getFlight_info_id() {
        return flight_info_id;
    }

    public void setFlight_info_id(String flight_info_id) {
        this.flight_info_id = flight_info_id;
    }

}
