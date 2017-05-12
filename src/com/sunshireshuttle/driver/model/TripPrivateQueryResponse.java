package com.sunshireshuttle.driver.model;

import java.util.ArrayList;

public class TripPrivateQueryResponse extends BaseResponseBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private boolean is_private;

    public boolean getIs_private() {
        return is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }
}
