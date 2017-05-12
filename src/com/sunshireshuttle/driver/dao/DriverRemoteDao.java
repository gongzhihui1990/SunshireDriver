package com.sunshireshuttle.driver.dao;

import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.http.HttpGetUtil;
import net.iaf.framework.http.HttpPostUtil;
import net.iaf.framework.http.HttpResult;
import net.iaf.framework.http.HttpsGetUtil;
import net.iaf.framework.http.HttpsPostUtil;
import net.iaf.framework.util.Loger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class DriverRemoteDao extends RemoteBaseDao {

    protected HashMap<String, String> hmParams = new HashMap<String, String>();
    boolean isUseHttpsPost = false;
    boolean debug = true;

    private final static void logUrl(String url, HashMap<String, String> hmParams) {
        String tempurl = url + "?";
        int i = 0;
        for (String key : hmParams.keySet()) {
            Loger.d(key + "=" + hmParams.get(key));
            if (i == 0) {
                tempurl += key + "=" + hmParams.get(key);
            } else {
                tempurl += "&" + key + "=" + hmParams.get(key);
            }
            i++;
        }
        Loger.d(tempurl);
    }

    private final static void logUrl(String url, String json) {
        String tempurl = url + "?" + json.toString();
        Loger.d(tempurl);
    }

    public HttpResult httpPost(String url, HashMap<String, String> hmParams)
            throws TimeoutException, NetworkException {
        if (debug) {
            logUrl(url, hmParams);
        }
        if (isUseHttpsPost) {
            return new HttpsPostUtil().executeRequest(url, hmParams);
        } else {
            return new HttpPostUtil().executeRequest(url, hmParams);
        }
    }

    public HttpResult httpsPost(String url, String json)
            throws TimeoutException, NetworkException {
        if (debug) {
            logUrl(url, json);
        }
        return new HttpsPostUtil().executeRequest(url, json);
    }

    public HttpResult httpPost(String url, String json)
            throws TimeoutException, NetworkException {
        if (debug) {
            logUrl(url, json);
        }
        if (isUseHttpsPost) {
            return new HttpsPostUtil().executeRequest(url, json);
        } else {
            return new HttpPostUtil().executeRequest(url, json);
        }
    }

    public HttpResult httpGet(String url, HashMap<String, String> hmParams)
            throws TimeoutException, NetworkException {
        if (debug) {
            logUrl(url, hmParams);
        }
        if (isUseHttpsPost) {
            return new HttpsGetUtil().executeRequest(url, hmParams);
        } else {
            return new HttpGetUtil().executeRequest(url, hmParams);
        }
    }

    protected void printParams(Map<String, String> hmParams) {
        for (String key : hmParams.keySet()) {
            Loger.d(key + "=" + hmParams.get(key));
        }
    }
}
