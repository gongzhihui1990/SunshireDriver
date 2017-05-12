package com.sunshireshuttle.driver.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
