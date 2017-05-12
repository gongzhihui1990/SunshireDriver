package com.sunshireshuttle.driver.model;

public class GetCustomerLocationResponse extends BaseResponseBean {

    private static final long serialVersionUID = 1L;

    private CustomerLocation location;

    public CustomerLocation getLocation() {
        return location;
    }

    public void setLocation(CustomerLocation location) {
        this.location = location;
    }
}
