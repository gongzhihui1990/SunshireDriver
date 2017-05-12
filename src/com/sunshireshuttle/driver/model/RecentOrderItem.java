package com.sunshireshuttle.driver.model;

import java.io.Serializable;

/**
 * @author Heisenberg heisenberg.gong@koolpos.com>
 * @ClassName: RecentOrder
 * @Description: 历史订单简要项
 * @date 2016年8月6日 下午9:15:05
 */

public class RecentOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String receivable;
    private String service_type;
    private String pickup_time;
    private String total;
    private String location_from;
    private String location_to;
    private String customer_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceivable() {
        return receivable;
    }

    public void setReceivable(String receivable) {
        this.receivable = receivable;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLocation_from() {
        return location_from;
    }

    public void setLocation_from(String location_from) {
        this.location_from = location_from;
    }

    public String getLocation_to() {
        return location_to;
    }

    public void setLocation_to(String location_to) {
        this.location_to = location_to;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

//    {
//        "id": "1673", 
//        "receivable": "0.00", 
//        "service_type": "EXCLUSIVE", 
//        "pickup_time": "2016-07-11 05:00:00", 
//        "total": "50.00", 
//        "location_from": "lax", 
//        "location_to": "33 e valley blvd alhambra", 
//        "customer_name": "ben"
//    }
}
