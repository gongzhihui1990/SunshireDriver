package com.sunshireshuttle.driver.dao;

import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.exception.TimeoutException;
import net.iaf.framework.http.HttpGetUtil;
import net.iaf.framework.http.HttpPostUtil;
import net.iaf.framework.http.HttpResult;
import net.iaf.framework.http.HttpsGetUtil;
import net.iaf.framework.http.HttpsPostUtil;

import java.util.HashMap;


public class RemoteBaseDao {

    protected HttpResult executeHttpGet(String url, HashMap<String, String> hmParams) throws TimeoutException,
            NetworkException {
        return new HttpGetUtil().executeRequest(url, hmParams);
    }

    protected HttpResult executeHttpGet(String url) throws TimeoutException, NetworkException {
        return new HttpGetUtil().executeRequest(url);
    }

    protected HttpResult executeHttpsGet(String url, HashMap<String, String> hmParams) throws TimeoutException,
            NetworkException {
        return new HttpsGetUtil().executeRequest(url, hmParams);
    }

    protected HttpResult executeHttpsGet(String url) throws TimeoutException, NetworkException {
        return new HttpsGetUtil().executeRequest(url);
    }

    protected HttpResult executeHttpPost(String url, HashMap<String, String> hmParams) throws TimeoutException,
            NetworkException {
        return new HttpPostUtil().executeRequest(url, hmParams);
    }

    protected HttpResult executeHttpPost(String url, String json) throws TimeoutException, NetworkException {
        return new HttpPostUtil().executeRequest(url, json);
    }

    protected HttpResult executeHttpsPost(String url, HashMap<String, String> hmParams) throws TimeoutException,
            NetworkException {
        return new HttpsPostUtil().executeRequest(url, hmParams);
    }
}
