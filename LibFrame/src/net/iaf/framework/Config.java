package net.iaf.framework;

import org.apache.http.protocol.HTTP;

public class Config {

    // ==============网络请求常量==========================
    /**
     * 网络连接超时值(单位ms)
     */
    public static final int TIMEOUT = 20000;

    /**
     * 网络请求参数编码
     */
    public static final String ENCODING = HTTP.UTF_8;

}
