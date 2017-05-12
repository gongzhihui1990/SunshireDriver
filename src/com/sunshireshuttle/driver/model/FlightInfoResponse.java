package com.sunshireshuttle.driver.model;

public class FlightInfoResponse extends BaseResponseBean {
    private static final long serialVersionUID = 1L;
    private FlightInfo flight;

    public FlightInfo getFlight() {
        return flight;
    }

    public void setFlight(FlightInfo flight) {
        this.flight = flight;
    }
}
