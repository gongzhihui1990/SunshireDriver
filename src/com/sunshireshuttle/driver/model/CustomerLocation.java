package com.sunshireshuttle.driver.model;

/**
 * @author Heisenberg heisenberg.gong@koolpos.com>
 * @ClassName: CustomerLocation
 * @Description: 客户当前位置
 * @date 2016年9月16日 下午7:25:57
 */

public class CustomerLocation extends BaseBean {
    private static final long serialVersionUID = 1L;
    private String passenger_name;
    private String id;
    private String product_id;
    private double latitude;
    private double longitude;
    private String reg_date;

    public String getPassenger_name() {
        return passenger_name;
    }

    public void setPassenger_name(String passenger_name) {
        this.passenger_name = passenger_name;
    }

    public String getDataId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

}
