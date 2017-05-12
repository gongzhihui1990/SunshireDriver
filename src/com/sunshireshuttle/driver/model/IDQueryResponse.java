package com.sunshireshuttle.driver.model;


public class IDQueryResponse extends BaseResponseBean {
    private static final long serialVersionUID = 1L;

    private String id;
    private DirverToken driver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DirverToken getDriver() {
        return driver;
    }

    public void setDriver(DirverToken driver) {
        this.driver = driver;
    }
}
