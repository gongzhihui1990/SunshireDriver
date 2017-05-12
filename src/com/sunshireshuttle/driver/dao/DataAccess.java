package com.sunshireshuttle.driver.dao;

import net.iaf.framework.exception.DBException;
import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.util.Loger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public abstract class DataAccess<T extends BaseJsonEntity<T>> {

    //缓存时间，单位毫秒
    protected long cacheTime = 0;
    //截至到“？”（包含“？”）的url字符串
    protected String urlWithoutParam = "";
    // 完整的请求url字符串（带ts,vkey等公共参数，包含接口请求所需的所有参数）
    protected String urlWhole = "";
    // 带参数的url字符串（不带ts,vkey等公共参数）
    protected String urlWithParam = "";
    //作为key的请求url字符串
    protected String urlCacheKey = "";
    //定制的key
    protected String urlCacheKeyCustom = null;

    protected BaseJsonEntity<T> jsonEntity;

    public DataAccess(BaseJsonEntity<T> jsonEntity) {
        this.jsonEntity = jsonEntity;
        this.urlWithoutParam = this.jsonEntity.getUrl();
        this.cacheTime = this.jsonEntity.getCacheTime() * 60000L;
    }

    public abstract T getData(HashMap<String, String> paramsMap, boolean cacheInvalidate)
            throws TimeoutException, NetworkException, DBException;

    protected void buildUrl(final HashMap<String, String> paramsMap) {
        this.urlWithParam = this.urlWithoutParam + paramMap2paramStr(paramsMap);
        this.urlCacheKey = (urlCacheKeyCustom == null) ? getString4CacheKey(this.urlWithoutParam, paramsMap) : this.urlCacheKeyCustom;
        this.urlWhole = this.urlWithParam + getDefaultParams(paramsMap);

//		Log.v("[urlWithoutParam]", urlWithoutParam);
//		Log.v("[urlWithParam]", urlWithParam);
        Loger.v("[urlWhole]:" + urlWhole);
//		Log.v("[urlCacheKey]", urlCacheKey);
    }

    protected String getString4CacheKey(String urlWithoutParam, final HashMap<String, String> paramsMap) {
        HashMap<String, String> paramsMap_ = new HashMap<String, String>();
        paramsMap_.putAll(paramsMap);
        paramsMap_.remove("timestamp");
        return this.urlWithoutParam + paramMap2paramStr(paramsMap_);
    }

    /**
     * @param @param  paramsMap
     * @param @return
     * @return String
     * @throws
     * @Title: customCacheKey
     * @Description: 需要构造特殊的urlCacheKey的情况在调用getData前调用该方法，传入拼接key的参数KV值
     */
    public void customCacheKey(HashMap<String, String> paramsMap) {
        if (null == paramsMap) {
            return;
        } else {
            this.urlCacheKeyCustom = this.urlWithoutParam + paramMap2paramStr(paramsMap);
        }
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: getDefaultParams
     * @Description: 拼接默认参数
     */
    protected String getDefaultParams(HashMap<String, String> paramsMap) {
        return "";
    }

    protected String paramMap2paramStr(final HashMap<String, String> paramsMap) {
        if (null == paramsMap || paramsMap.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        //排序,保证拼出的String一致
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(paramsMap.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                //return (o2.getValue() - o1.getValue());  //根据Value排序
                return (o1.getKey()).toString().compareTo(o2.getKey()); //根据Key排序
            }
        });

        boolean isFirstParam = true;
        for (Map.Entry<String, String> entry : infoIds) {
            if (!isFirstParam) {
                sb.append("&");
            } else {
                isFirstParam = false;
            }
            try {
                sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                sb.append("=");
                String value = entry.getValue();
                if (value == null) {
                    value = "";
                }
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }
        }

        String str = sb.toString();
        if (str.length() <= 1) {
            return "";
        }
        return str;
    }

}