package com.sunshireshuttle.driver.model;

public class BaseResponseBean extends BaseBean {

    private static final long serialVersionUID = 6020954887457571520L;
    private boolean status;
    private String message;

    public boolean isResponseOK() {
        if (status) {
            return true;
        }
        return false;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
